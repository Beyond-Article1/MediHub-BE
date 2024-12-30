package mediHub_be.chatbot.service;

import mediHub_be.config.ChatbotConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    private static final String DEFAULT_SYSTEM_PROMPT = "당신은 데이터베이스 기반의 의료 지식 시스템에서의 지능형 챗봇입니다. 데이터를 기반으로 정확한 답변을 제공합니다.";
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
            String context = buildPrompt(question, searchResults);

            // OpenAI API 요청 본문 구성
            Map<String, Object> body = Map.of(
                    "model", OPENAI_MODEL,
                    "messages", List.of(
                            Map.of("role", "system", "content", DEFAULT_SYSTEM_PROMPT),
                            Map.of("role", "user", "content", context)
                    ),
                    "max_tokens", 300, // 최대 토큰 수를 늘려 상세 답변 가능
                    "temperature", 0.7 // 답변의 창의성과 정확성 조정
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            // OpenAI API 호출
            ResponseEntity<Map> response = restTemplate.exchange(
                    chatbotConfig.getApiUrl(), HttpMethod.POST, request, Map.class
            );

            // 응답 처리
            return processResponse(response);

        } catch (Exception e) {
            // 예외 발생 시 로그 출력 및 기본 메시지 반환
            e.printStackTrace();
            return "OpenAI API 호출 중 오류가 발생했습니다: " + e.getMessage();
        }
    }

    /**
     * 검색 결과를 기반으로 프롬프트를 생성
     */
    private String buildPrompt(String question, List<Map<String, String>> searchResults) {
        StringBuilder context = new StringBuilder("다음은 데이터베이스에서 검색된 데이터입니다:\n\n");

        // 상위 5개의 검색 결과만 포함
        for (int i = 0; i < Math.min(searchResults.size(), 5); i++) {
            Map<String, String> result = searchResults.get(i);
            context.append("테이블 이름: ").append(result.getOrDefault("table", "알 수 없음")).append("\n");
            context.append("행 ID: ").append(result.getOrDefault("rowId", "알 수 없음")).append("\n");
            context.append("내용: ").append(result.getOrDefault("content", "없음")).append("\n\n");
        }

        if (searchResults.size() > 5) {
            context.append("추가 데이터가 존재합니다. 상위 5개 결과만 표시합니다.\n\n");
        }

        context.append("질문: ").append(question).append("\n");
        context.append("위 데이터를 기반으로 정확한 답변을 생성하세요.");
        return context.toString();
    }

    /**
     * OpenAI API 응답 처리
     */
    private String processResponse(ResponseEntity<Map> response) {
        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null || !responseBody.containsKey("choices")) {
            return DEFAULT_ERROR_MESSAGE;
        }

        // 응답에서 'choices' 필드 추출
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        if (choices.isEmpty()) {
            return DEFAULT_ERROR_MESSAGE;
        }

        // 첫 번째 선택지의 'content' 반환
        Map<String, Object> choice = choices.get(0);
        Map<String, Object> message = (Map<String, Object>) choice.get("message");
        return (String) message.get("content");
    }
}
