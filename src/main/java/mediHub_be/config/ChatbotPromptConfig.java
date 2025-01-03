package mediHub_be.config;

import lombok.Data;

import java.util.Map;
import java.util.List;

@Data
public class ChatbotPromptConfig {
    private SystemConfig system;
    private Map<String, TableConfig> tables;

    @Data
    public static class SystemConfig {
        private String description;
        private List<Example> examples;
    }

    @Data
    public static class TableConfig {
        private String description;
        private List<String> columns;
        private List<String> filters;
        private String mapping;
    }

    @Data
    public static class Example { // 새로운 Example 클래스 추가
        private String question; // 예시 질문
        private String answer;   // 예시 답변
    }
}
