package mediHub_be.chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.chatbot.dto.ChatbotRequestDTO;
import mediHub_be.chatbot.dto.ChatbotResponseDTO;
import mediHub_be.chatbot.dto.MessageDTO;
import mediHub_be.config.ChatbotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotService {

    private final RestTemplate restTemplate;
    private final ChatbotConfig chatbotConfig;

    public String getChatbotResponse (String userMessage) {
        // 1. 요청 데이터 생성
        ChatbotRequestDTO request = createChatRequest(userMessage);
        
        //2. HTTP 헤더 설정
        HttpHeaders headers = createHeaders();

        // 3. HTTP 요청 생성
        HttpEntity<ChatbotRequestDTO> httpEntity = new HttpEntity<>(request, headers);

        try{
            //4. API 호출 및 응답 처리
            ResponseEntity<ChatbotResponseDTO> responseEntity = restTemplate.postForEntity(
                    chatbotConfig.getApiUrl(),
                    httpEntity,
                    ChatbotResponseDTO.class
            );

            return extractResponse(responseEntity);
        } catch (Exception e) {
            log.error("ChatGPT API 호출 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("ChatGPT API 호출 실패");
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + chatbotConfig.getChatbotApiKey()); // API 키 설정
        headers.set("Content-Type", "application/json"); // JSON 형식 지정
        return headers;
    }

    private ChatbotRequestDTO createChatRequest(String userMessage) {
        return new ChatbotRequestDTO(
                "gpt-3.5-turbo", // 사용할 모델 - 더 저렴함..
                List.of(new MessageDTO("user", userMessage))
        );
    }

    private String extractResponse(ResponseEntity<ChatbotResponseDTO> responseEntity) {
        ChatbotResponseDTO response = responseEntity.getBody();
        if (response != null && !response.getChoices().isEmpty()) {
            return response.getChoices().get(0).getMessage().getContent();
        } else {
            throw new RuntimeException("ChatGPT API에서 유효한 응답을 받지 못했습니다.");
        }
    }

}
