package mediHub_be.chatbot.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Service
@Slf4j
public class VectorizationService {

    private final TableDataService tableDataService;
    private final EmbeddingService embeddingService;
    private final VectorDatabase vectorDatabase;

    // 유효한 테이블 이름 목록
    private static final Set<String> VALID_TABLES = new HashSet<>(Arrays.asList(
            "anonymous_board",
            "case_sharing",
            "cp",
            "flag",
            "keyword",
            "medical_life",
            "dept",
            "part",
            "user"
    ));

    public void processAllTables() {
        try {
            vectorDatabase.clearVectorDatabase();
            System.out.println("Vector database cleared successfully.");

            // 1. 데이터베이스에서 모든 테이블 이름 가져오기
            List<String> tableNames = tableDataService.getAllTableNames();

            // 2. 유효한 테이블 이름 필터링
            tableNames = tableNames.stream()
                    .filter(VALID_TABLES::contains)
                    .toList();

            // 3. 각 테이블 처리
            for (String tableName : tableNames) {
                try {
                    processTable(tableName);
                    System.out.println("Successfully processed table: " + tableName);
                } catch (Exception e) {
                    System.err.println("Error processing table: " + tableName);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processTable(String tableName) {
        if (!VALID_TABLES.contains(tableName)) {
            log.warn("Skipping invalid table: {}", tableName);
            return;
        }

        try {
            // 테이블의 데이터 가져오기
            List<Map<String, Object>> tableData = tableDataService.getTableData(tableName);

            // 테이블의 Primary Key 가져오기
            String primaryKey = getPrimaryKey(tableName);
            if (primaryKey == null) {
                log.warn("No primary key found for table: {}", tableName);
                return;
            }

            // 테이블 데이터 처리
            for (Map<String, Object> row : tableData) {
                try {
                    // Row ID 가져오기
                    String rowId = String.valueOf(row.get(primaryKey));
                    String content = row.values().toString(); // 모든 데이터를 텍스트로 결합

                    // OpenAI 임베딩 생성
                    List<Float> vector = embeddingService.getEmbedding(content);

                    // 벡터 데이터 저장
                    vectorDatabase.storeVector(tableName, rowId, content, vector, new ArrayList<>(row.keySet()));
                    log.info("Stored vector for table: {}, row ID: {}", tableName, rowId);
                } catch (Exception e) {
                    log.error("Error processing row in table: {}", tableName, e);
                }
            }
        } catch (Exception e) {
            log.error("Error processing table: {}", tableName, e);
        }
    }

    public String getPrimaryKey(String tableName) {
        try (Connection connection = tableDataService.getDataSource().getConnection();
             ResultSet primaryKeys = connection.getMetaData().getPrimaryKeys(null, null, tableName)) {

            if (primaryKeys.next()) {
                String primaryKey = primaryKeys.getString("COLUMN_NAME"); // Primary Key 컬럼 이름 반환
                log.info("Found primary key for table {}: {}", tableName, primaryKey);
                return primaryKey;
            } else {
                log.warn("No primary key found for table: {}", tableName);
            }
        } catch (Exception e) {
            log.error("Error fetching primary key for table: {}", tableName, e);
        }
        return null; // Primary Key가 없는 경우 null 반환
    }



}