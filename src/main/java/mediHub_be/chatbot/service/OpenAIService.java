package mediHub_be.chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import mediHub_be.config.ChatbotPromptConfig;
import mediHub_be.config.ChatbotConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OpenAIService {

    private static final String DEFAULT_ERROR_MESSAGE = "답변을 생성하지 못했습니다.";
    private static final String OPENAI_MODEL = "gpt-4";

    private final ChatbotConfig chatbotConfig;
    private final ChatbotPromptLoader promptLoader;

    public String generateAnswer(String question, List<Map<String, String>> searchResults) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + chatbotConfig.getChatbotApiKey());

        try {
            // JSON 프롬프트 데이터 로드
            ChatbotPromptConfig promptConfig = promptLoader.getPromptConfig();

            // 프롬프트 생성
            String systemMessage = buildSystemMessage(promptConfig);
            String prompt = buildPrompt(promptConfig, question, searchResults);

            // OpenAI API 요청 본문 구성
            Map<String, Object> body = Map.of(
                    "model", OPENAI_MODEL,
                    "messages", List.of(
                            Map.of("role", "system", "content", systemMessage),
                            Map.of("role", "user", "content", prompt)
                    ),
                    "max_tokens", 500,
                    "temperature", 0.7
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            // OpenAI API 호출
            ResponseEntity<Map> response = restTemplate.exchange(
                    chatbotConfig.getApiUrl(), HttpMethod.POST, request, Map.class
            );

            // 응답 처리
            return processResponse(response);

        } catch (Exception e) {
            e.printStackTrace();
            return "OpenAI API 호출 중 오류가 발생했습니다: " + e.getMessage();
        }
    }


    private String buildSystemMessage(ChatbotPromptConfig config) {
        StringBuilder systemMessage = new StringBuilder();

        // 시스템 설명 추가
        systemMessage.append(config.getSystem().getDescription());
        systemMessage.append("\n\n[사용 예제와 답변]\n");

        // 예제와 답변 추가
        for (ChatbotPromptConfig.Example example : config.getSystem().getExamples()) {
            systemMessage.append("- 질문: ").append(example.getQuestion()).append("\n");
            systemMessage.append("  답변: ").append(example.getAnswer()).append("\n");
        }

        return systemMessage.toString();
    }


    private String truncateText(String text) {
        if (text.length() <= 8000) {
            return text;
        }
        return text.substring(0, 8000) + "..."; // 최대 길이 초과 시 축약
    }


    private String buildPrompt(ChatbotPromptConfig config, String question, List<Map<String, String>> searchResults) {
        StringBuilder prompt = new StringBuilder("질문: ").append(question).append("\n\n");
        prompt.append("[검색 결과]\n");

        // 검색 결과를 프롬프트에 추가
        for (int i = 0; i < searchResults.size(); i++) {
            Map<String, String> result = searchResults.get(i);
            prompt.append("결과 ").append(i + 1).append(":\n");
            result.forEach((key, value) -> prompt.append("- ").append(key).append(": ").append(value).append("\n"));
            prompt.append("\n");
        }

        // 테이블 정보 추가
        prompt.append("\n[테이블 정보]\n");
        for (Map.Entry<String, ChatbotPromptConfig.TableConfig> entry : config.getTables().entrySet()) {
            ChatbotPromptConfig.TableConfig tableConfig = entry.getValue();
            prompt.append("테이블 이름: ").append(entry.getKey()).append("\n");
            prompt.append("설명: ").append(tableConfig.getDescription()).append("\n");
            if (tableConfig.getColumns() != null) {
                prompt.append("컬럼:\n");
                tableConfig.getColumns().forEach(column -> prompt.append("- ").append(column).append("\n"));
            }
            if (tableConfig.getFilters() != null) {
                prompt.append("\n필터:\n");
                tableConfig.getFilters().forEach(filter -> prompt.append("- ").append(filter).append("\n"));
            }
            if (tableConfig.getMapping() != null) {
                prompt.append("\n매핑: ").append(tableConfig.getMapping()).append("\n");
            }
            prompt.append("\n");
        }

        // 트림 길이를 초과할 경우 잘라내기
        return truncateText(prompt.toString());
    }


    private String processResponse(ResponseEntity<Map> response) {
        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null || !responseBody.containsKey("choices")) {
            return DEFAULT_ERROR_MESSAGE;
        }

        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        if (choices.isEmpty()) {
            return DEFAULT_ERROR_MESSAGE;
        }

        Map<String, Object> choice = choices.get(0);
        Map<String, Object> message = (Map<String, Object>) choice.get("message");
        return (String) message.get("content");
    }
}
