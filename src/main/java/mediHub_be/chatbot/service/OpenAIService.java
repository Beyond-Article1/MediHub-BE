package mediHub_be.chatbot.service;

import mediHub_be.config.ChatbotConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    private static final String DEFAULT_SYSTEM_PROMPT = "당신은 데이터베이스 기반의 의료 지식 시스템에서의 지능형 챗봇입니다.";
    private static final String DEFAULT_ERROR_MESSAGE = "답변을 생성하지 못했습니다.";
    private static final String OPENAI_MODEL = "gpt-4";

    private final ChatbotConfig chatbotConfig;

    public OpenAIService(ChatbotConfig chatbotConfig) {
        this.chatbotConfig = chatbotConfig;
    }

    public String generateAnswer(String question, List<Map<String, String>> searchResults) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + chatbotConfig.getChatbotApiKey());

        try {
            // 검색된 데이터를 기반으로 프롬프트 생성
            StringBuilder context = new StringBuilder("다음은 검색된 데이터입니다:\n");
            if (searchResults != null && !searchResults.isEmpty()) {
                for (Map<String, String> result : searchResults) {
                    context.append("Row ID: ").append(result.getOrDefault("rowId", "N/A")).append("\n");
                    context.append("Metadata: ").append(result.getOrDefault("metadata", "N/A")).append("\n\n");
                }
            } else {
                context.append("검색된 데이터가 없습니다.\n");
            }
            context.append("질문: ").append(question).append("\n답변:");

            // OpenAI API 요청 본문 구성
            Map<String, Object> body = Map.of(
                    "model", OPENAI_MODEL,
                    "messages", List.of(
                            Map.of("role", "system", "content", DEFAULT_SYSTEM_PROMPT),
                            Map.of("role", "user", "content", context.toString())
                    ),
                    "max_tokens", 200
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            // OpenAI API 호출
            ResponseEntity<Map> response = restTemplate.exchange(
                    chatbotConfig.getApiUrl(), HttpMethod.POST, request, Map.class
            );

            // 응답 처리
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    return (String) ((Map<String, Object>) choice.get("message")).get("content");
                }
            }

            // 응답에 choices가 없는 경우
            return DEFAULT_ERROR_MESSAGE;

        } catch (Exception e) {
            // 예외 발생 시 로그 출력 및 기본 메시지 반환
            e.printStackTrace();
            return "OpenAI API 호출 중 오류가 발생했습니다: " + e.getMessage();
        }
    }
}
