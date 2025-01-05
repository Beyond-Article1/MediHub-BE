package mediHub_be.chatbot.service;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.*;

@AllArgsConstructor
@Service
public class TableDataService {

    private final JdbcTemplate jdbcTemplate;

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

    // 모든 테이블 이름 가져오기 (필터링 적용)
    public List<String> getAllTableNames() {
        List<String> tableNames = new ArrayList<>();
        try {
            DatabaseMetaData metaData = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection().getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                if (VALID_TABLES.contains(tableName)) { // 유효한 테이블만 추가
                    tableNames.add(tableName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableNames;
    }

    // 특정 테이블의 데이터 가져오기
    public List<Map<String, Object>> getTableData(String tableName) {
        if (!VALID_TABLES.contains(tableName)) {
            throw new IllegalArgumentException("Invalid table name: " + tableName);
        }
        try {
            String query = "SELECT * FROM " + tableName;
            return jdbcTemplate.queryForList(query);
        } catch (Exception e) {
            System.err.println("Error processing table: " + tableName);
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public DataSource getDataSource() {
        return jdbcTemplate.getDataSource();
    }



}