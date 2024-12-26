package mediHub_be.chatbot.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Service
@Slf4j
public class VectorizationService {

    private final TableDataService allTableDataService;
    private final EmbeddingService embeddingService;
    private final VectorDatabase vectorDatabase;

    // 병렬 작업을 위한 스레드 풀 생성 (CPU 코어 수에 맞게 설정)
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void processAllTables() {
        try {
            vectorDatabase.clearVectorDatabase();
            log.info("Vector database cleared successfully.");

            // 1. 데이터베이스에서 모든 테이블 이름 가져오기
            List<String> tableNames = allTableDataService.getAllTableNames();

            // 2. 병렬로 각 테이블 처리
            for (String tableName : tableNames) {
                executorService.submit(() -> {
                    try {
                        processTable(tableName);
                        log.info("Successfully processed table: {}", tableName);
                    } catch (Exception e) {
                        log.error("Error processing table: {}", tableName, e);
                    }
                });
            }

        } catch (Exception e) {
            log.error("Error in processAllTables", e);
        } finally {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.warn("ExecutorService did not terminate in the specified time.");
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                log.error("ExecutorService termination interrupted.", e);
                executorService.shutdownNow();
            }
            log.info("ExecutorService shut down successfully.");
        }
    }

    private String getPrimaryKey(String tableName) {
        try (Connection connection = allTableDataService.getDataSource().getConnection();
             ResultSet primaryKeys = connection.getMetaData().getPrimaryKeys(null, null, tableName)) {

            if (primaryKeys.next()) {
                return primaryKeys.getString("COLUMN_NAME"); // Primary Key 컬럼 이름 반환
            } else {
                log.warn("No primary key found for table: {}", tableName);
            }
        } catch (Exception e) {
            log.error("Error fetching primary key for table: {}", tableName, e);
        }
        return null; // Primary Key가 없는 경우 null 반환
    }

    private List<String> getColumnNames(String tableName) {
        try (Connection connection = allTableDataService.getDataSource().getConnection();
             ResultSet columns = connection.getMetaData().getColumns(null, null, tableName, null)) {

            List<String> columnNames = new ArrayList<>();
            while (columns.next()) {
                columnNames.add(columns.getString("COLUMN_NAME"));
            }
            return columnNames;

        } catch (Exception e) {
            log.error("Error fetching column names for table: {}", tableName, e);
        }
        return List.of(); // 컬럼 이름을 가져올 수 없으면 빈 리스트 반환
    }

    private void processTable(String tableName) {
        try {
            // 1. 특정 테이블의 데이터 가져오기
            List<Map<String, Object>> tableData = allTableDataService.getTableData(tableName);

            // 2. 테이블의 컬럼 이름(메타데이터) 가져오기
            List<String> columnNames = getColumnNames(tableName);

            String primaryKey = getPrimaryKey(tableName);

            for (Map<String, Object> row : tableData) {
                try {
                    // 3. 데이터 텍스트화 (모든 컬럼 데이터를 병합)
                    String rowSeq = primaryKey != null ? String.valueOf(row.get(primaryKey)) : "null";
                    String content = row.values().toString(); // 모든 데이터를 텍스트로 결합

                    // 4. OpenAI 임베딩 API 호출
                    List<Float> vector = embeddingService.getEmbedding(content);

                    // 5. 벡터 데이터 저장 (메타데이터 포함)
                    vectorDatabase.storeVector(tableName, rowSeq, content, vector, columnNames);
                    log.info("Stored vector for table: {}, row ID: {}", tableName, rowSeq);

                } catch (IOException e) {
                    log.error("Error storing vector for row in table: {}", tableName, e);
                }
            }

        } catch (Exception e) {
            log.error("Error processing table: {}", tableName, e);
        }
    }

    public void logAllVectorizedData() {
        try {
            List<Map<String, Object>> vectorizedData = vectorDatabase.getAllVectors();

            // 데이터를 로그로 출력
            log.info("Logging all vectorized data...");
            for (Map<String, Object> data : vectorizedData) {
                log.info("Table: {}, Row ID: {}, Content: {}, Vector: {}, Metadata: {}",
                        data.get("table"), data.get("rowId"), data.get("content"), data.get("vector"), data.get("metadata"));
            }
            log.info("Finished logging all vectorized data.");
        } catch (Exception e) {
            log.error("Error logging vectorized data.", e);
        }
    }
}
