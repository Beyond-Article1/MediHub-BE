package mediHub_be.chatbot.service;

import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class VectorDatabase {

    private final IndexWriter indexWriter;
    private final IndexSearcher indexSearcher;

    public VectorDatabase() throws IOException {
        // Lucene 디렉토리 생성
        FSDirectory directory = FSDirectory.open(Paths.get("vector-index"));
        IndexWriterConfig config = new IndexWriterConfig();
        this.indexWriter = new IndexWriter(directory, config);

        // IndexSearcher 초기화
        DirectoryReader reader = DirectoryReader.open(indexWriter);
        this.indexSearcher = new IndexSearcher(reader);
        BooleanQuery.setMaxClauseCount(10000);
    }

    /**
     * 벡터와 메타데이터를 저장
     */
    public void storeVector(String tableName, String rowId, String content, List<Float> vector, List<String> columnNames) throws IOException {
        Document document = new Document();

        // 테이블 이름 및 Row ID 저장
        document.add(new StringField("table", tableName, Field.Store.YES));
        document.add(new StringField("id", rowId, Field.Store.YES));

        // 컬럼 데이터와 컬럼 이름을 JSON 형식으로 결합하여 content에 저장
        StringBuilder contentWithColumns = new StringBuilder();
        for (int i = 0; i < columnNames.size(); i++) {
            contentWithColumns.append(columnNames.get(i))
                    .append(": ")
                    .append(content.split(",")[i].trim()) // 데이터와 매칭
                    .append("\n");
        }
        document.add(new TextField("content", contentWithColumns.toString(), Field.Store.YES));

        // 벡터 저장
        for (int i = 0; i < vector.size(); i++) {
            document.add(new FloatPoint("vector_" + i, vector.get(i)));
            document.add(new StoredField("vector_" + i, vector.get(i)));
        }

        indexWriter.addDocument(document);
        indexWriter.commit();
        System.out.println("Stored vector for row ID: " + rowId + " with metadata.");
    }



    /**
     * 벡터 검색
     */
    public List<Map<String, String>> search(float[] queryVector, int topK) throws IOException {
        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();

        // 각 차원의 범위 쿼리 추가 (±0.1 유사도 범위)
        for (int i = 0; i < queryVector.length; i++) {
            queryBuilder.add(
                    FloatPoint.newRangeQuery("vector_" + i, queryVector[i] - 0.1f, queryVector[i] + 0.1f),
                    BooleanClause.Occur.MUST
            );
        }

        // 최종 쿼리 생성
        Query query = queryBuilder.build();
        TopDocs topDocs = indexSearcher.search(query, topK);

        // 결과 처리
        List<Map<String, String>> results = new ArrayList<>();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            Map<String, String> result = new HashMap<>();
            result.put("rowId", doc.get("id"));
            result.put("table", doc.get("table"));
            result.put("content", doc.get("content"));
            result.put("score", String.valueOf(scoreDoc.score));
            results.add(result);
        }
        System.out.println("검색 결과: " + results);
        return results;
    }

    /**
     * 리소스 해제
     */
    public void close() throws IOException {
        indexWriter.close();
    }

    public void clearVectorDatabase() {
        try {
            indexWriter.deleteAll(); // 모든 문서 삭제
            indexWriter.commit();   // 삭제 적용
            System.out.println("Vector database has been cleared.");
        } catch (IOException e) {
            System.err.println("Error clearing vector database.");
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> getAllVectors() throws IOException {
        List<Map<String, Object>> vectors = new ArrayList<>();
        try (IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("vector-index")))) {
            for (int i = 0; i < reader.maxDoc(); i++) {
                Document doc = reader.document(i);
                Map<String, Object> vectorData = new HashMap<>();
                vectorData.put("table", doc.get("table"));
                vectorData.put("rowId", doc.get("id"));
                vectorData.put("content", doc.get("content"));

                List<Float> vector = new ArrayList<>();
                for (int j = 0; doc.getField("vector_" + j) != null; j++) {
                    vector.add(Float.valueOf(doc.get("vector_" + j)));
                }
                vectorData.put("vector", vector);

                vectors.add(vectorData);
            }
        }
        return vectors;
    }
}
