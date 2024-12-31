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

    // 모든 테이블 이름 가져오기
    public List<String> getAllTableNames() {
        List<String> tableNames = new ArrayList<>();
        try {
            DatabaseMetaData metaData = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection().getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                tableNames.add(tables.getString("TABLE_NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableNames;
    }

    // 특정 테이블의 데이터 가져오기
    public List<Map<String, Object>> getTableData(String tableName) {
        try {
            String query = "SELECT * FROM " + tableName; // SQL Injection 주의 필요
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
