package mediHub_be.chatbot.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import mediHub_be.dept.repository.DeptRepository;
import mediHub_be.part.repository.PartRepository;
import mediHub_be.ranking.repository.RankingRepository;
import mediHub_be.user.repository.UserRepository;
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

@RequiredArgsConstructor
@Component
public class VectorDatabase {

    private final PartRepository partRepository;
    private final DeptRepository deptRepository;
    private final RankingRepository rankingRepository;
    private final UserRepository userRepository;

    private IndexWriter indexWriter;
    private IndexSearcher indexSearcher;

    @PostConstruct
    public void init() throws IOException {
        // Lucene 디렉토리 생성 및 IndexWriter 초기화
        FSDirectory directory = FSDirectory.open(Paths.get("vector-index"));
        IndexWriterConfig config = new IndexWriterConfig();
        this.indexWriter = new IndexWriter(directory, config);

        // IndexSearcher 초기화
        refreshIndexSearcher();
        BooleanQuery.setMaxClauseCount(10000);
    }

    public void storeVector(String tableName, String rowId, String content, List<Float> vector, List<String> columnNames, Map<String, String> foreignKeyData) throws IOException {
        Document document = new Document();

        // 테이블 이름 및 Row ID 저장
        if (rowId == null || rowId.trim().isEmpty()) {
            throw new IllegalArgumentException("Row ID가 null이거나 비어 있습니다. 테이블: " + tableName);
        }
        document.add(new StringField("table", tableName, Field.Store.YES));
        document.add(new StringField("id", rowId, Field.Store.YES));

        // JSON 형태로 컬럼 데이터 저장
        StringBuilder contentWithColumns = new StringBuilder();
        String[] contentValues = content.split(",");
        for (int i = 0; i < columnNames.size(); i++) {
            contentWithColumns.append(columnNames.get(i))
                    .append(": ")
                    .append(i < contentValues.length ? contentValues[i].trim() : "null")
                    .append("\n");
        }

        // 외래키 데이터 추가
        if (foreignKeyData != null) {
            foreignKeyData.forEach((key, value) -> contentWithColumns.append(key).append(": ").append(value).append("\n"));
        }

        document.add(new TextField("content", contentWithColumns.toString(), Field.Store.YES));

        // 벡터 저장
        float magnitude = 0.0f;
        for (int i = 0; i < vector.size(); i++) {
            float value = vector.get(i);
            magnitude += value * value;
            document.add(new FloatPoint("vector_" + i, value));
            document.add(new StoredField("vector_" + i, value));
        }
        magnitude = (float) Math.sqrt(magnitude);
        document.add(new StoredField("magnitude", magnitude));

        // 디버깅 로그 추가
        System.out.println("문서 저장 - 테이블: " + tableName + ", Row ID: " + rowId + ", Magnitude: " + magnitude + ", Vector: " + vector);

        indexWriter.addDocument(document);
        indexWriter.commit();
        refreshIndexSearcher();
    }


    public Map<String, String> getForeignKeyData(Map<String, Object> rowData) {
        Map<String, String> foreignKeyData = new HashMap<>();

        // 외래키와 참조 테이블 매핑
        Map<String, String> foreignKeyMapping = Map.of(
                "part_seq", "part_name",
                "dept_seq", "dept_name",
                "ranking_seq", "ranking_name",
                "user_seq", "user_name,user_id"
        );

        for (Map.Entry<String, String> entry : foreignKeyMapping.entrySet()) {
            String foreignKey = entry.getKey();
            String[] referenceFields = entry.getValue().split(",");

            // 외래키 값 가져오기
            if (rowData.containsKey(foreignKey)) {
                Long foreignKeyValue = (Long) rowData.get(foreignKey);

                // 참조 데이터 조회 (예: PartRepository 등 사용)
                Map<String, String> referencedValues = getReferencedValues(foreignKey, foreignKeyValue);

                for (String field : referenceFields) {
                    if (referencedValues.containsKey(field)) {
                        foreignKeyData.put(field, referencedValues.get(field));
                    }
                }
            }
        }

        return foreignKeyData;
    }

    public Map<String, String> getReferencedValues(String foreignKey, Long foreignKeyValue) {
        Map<String, String> referencedValues = new HashMap<>();

        switch (foreignKey) {
            case "part_seq":
                partRepository.findById(foreignKeyValue).ifPresent(part ->
                        referencedValues.put("part_name", part.getPartName()));
                break;
            case "dept_seq":
                deptRepository.findById(foreignKeyValue).ifPresent(dept ->
                        referencedValues.put("dept_name", dept.getDeptName()));
                break;
            case "ranking_seq":
                rankingRepository.findById(foreignKeyValue).ifPresent(ranking ->
                        referencedValues.put("ranking_name", ranking.getRankingName()));
                break;
            case "user_seq":
                userRepository.findById(foreignKeyValue).ifPresent(user -> {
                    referencedValues.put("user_name", user.getUserName());
                    referencedValues.put("user_id", user.getUserId());
                });
                break;
        }

        return referencedValues;
    }


    public List<Map<String, String>> search(float[] queryVector, int topK) throws IOException {
        List<Map<String, String>> results = new ArrayList<>();

        // 쿼리 벡터의 크기 계산
        float queryMagnitude = 0.0f;
        for (float value : queryVector) {
            queryMagnitude += value * value;
        }
        queryMagnitude = (float) Math.sqrt(queryMagnitude);

        TopDocs topDocs = indexSearcher.search(new MatchAllDocsQuery(), Integer.MAX_VALUE);

        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);

            // 저장된 벡터 가져오기
            List<Float> storedVector = new ArrayList<>();
            for (int i = 0; ; i++) {
                IndexableField field = doc.getField("vector_" + i);
                if (field == null) break;
                storedVector.add(field.numericValue().floatValue());
            }

            // 저장된 벡터 크기 가져오기
            float storedMagnitude = doc.getField("magnitude").numericValue().floatValue();

            // 코사인 유사도 계산
            float dotProduct = 0.0f;
            for (int i = 0; i < queryVector.length; i++) {
                dotProduct += queryVector[i] * storedVector.get(i);
            }
            float cosineSimilarity = dotProduct / (queryMagnitude * storedMagnitude);

            // 결과 추가
            Map<String, String> result = new HashMap<>();
            result.put("rowId", doc.get("id"));
            result.put("table", doc.get("table"));
            result.put("content", doc.get("content"));
            result.put("score", String.valueOf(cosineSimilarity));
            results.add(result);
        }

        // 코사인 유사도 기준으로 정렬
        results.sort((a, b) -> Float.compare(Float.parseFloat(b.get("score")), Float.parseFloat(a.get("score"))));

        // 상위 K개 결과 반환
        return results.subList(0, Math.min(topK, results.size()));
    }

    /**
     * 벡터 데이터베이스 초기화
     */
    public void clearVectorDatabase() {
        try {
            indexWriter.deleteAll(); // 모든 문서 삭제
            indexWriter.commit();   // 삭제 적용
            refreshIndexSearcher();
            System.out.println("Vector database has been cleared.");
        } catch (IOException e) {
            System.err.println("Error clearing vector database.");
            e.printStackTrace();
        }
    }

    /**
     * IndexSearcher 갱신
     */
    private void refreshIndexSearcher() throws IOException {
        DirectoryReader currentReader = (indexSearcher != null && indexSearcher.getIndexReader() instanceof DirectoryReader)
                ? (DirectoryReader) indexSearcher.getIndexReader()
                : null;

        DirectoryReader newReader = (currentReader != null)
                ? DirectoryReader.openIfChanged(currentReader)
                : DirectoryReader.open(indexWriter);

        if (newReader != null) {
            if (indexSearcher != null) {
                indexSearcher.getIndexReader().close(); // 기존 리더 닫기
            }
            indexSearcher = new IndexSearcher(newReader); // 새로운 검색자 초기화
        }
    }

    /**
     * 리소스 해제
     */
    public void close() throws IOException {
        indexWriter.close();
        if (indexSearcher != null) {
            indexSearcher.getIndexReader().close();
        }
    }
}