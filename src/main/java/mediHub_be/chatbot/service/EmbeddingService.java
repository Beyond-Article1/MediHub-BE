package mediHub_be.chatbot.service;

import mediHub_be.config.ChatbotConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class EmbeddingService {

    private final ChatbotConfig chatbotConfig;

    private static final String OPENAI_EMBEDDING_URL = "https://api.openai.com/v1/embeddings";

    public EmbeddingService(ChatbotConfig chatbotConfig) {
        this.chatbotConfig = chatbotConfig;
    }

    public List<Float> getEmbedding(String inputText) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + chatbotConfig.getChatbotApiKey());

        // 요청 본문 생성
        Map<String, Object> body = new HashMap<>();
        body.put("model", "text-embedding-ada-002");
        body.put("input", inputText);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // OpenAI 임베딩 API 호출
        ResponseEntity<Map> response = restTemplate.exchange(
                OPENAI_EMBEDDING_URL, HttpMethod.POST, request, Map.class
        );

        Map responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("data")) {
            // 임베딩 데이터 가져오기
            Map<String, Object> embeddingData = ((List<Map<String, Object>>) responseBody.get("data")).get(0);

            // Double 리스트를 Float 리스트로 변환
            return convertToFloatList((List<Double>) embeddingData.get("embedding"));
        }
        return Collections.emptyList();
    }

    // Double 리스트를 Float 리스트로 변환하는 메서드
    private List<Float> convertToFloatList(List<Double> doubleList) {
        List<Float> floatList = new ArrayList<>();
        for (Double value : doubleList) {
            floatList.add(value.floatValue()); // Double → Float 변환
        }
        return floatList;
    }
}
