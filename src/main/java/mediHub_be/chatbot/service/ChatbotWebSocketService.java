package mediHub_be.chatbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.chatbot.dto.ChatbotRequestDTO;
import mediHub_be.chatbot.dto.ChatbotResponseDTO;
import mediHub_be.chatbot.dto.MessageDTO;
import mediHub_be.config.ChatbotConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotWebSocketService {

    private final RestTemplate restTemplate;
    private final ChatbotConfig chatbotConfig;

    public String getChatbotResponse(String userMessage) {
        ChatbotRequestDTO request = createChatRequest(userMessage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + chatbotConfig.getChatbotApiKey());

        HttpEntity<ChatbotRequestDTO> entity = new HttpEntity<>(request, headers);

        ChatbotResponseDTO response = restTemplate.postForObject(
                chatbotConfig.getApiUrl(),
                entity,
                ChatbotResponseDTO.class
        );

        if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
            return response.getChoices().get(0).getMessage().getContent();
        } else {
            throw new RuntimeException("ChatGPT 응답 없음");
        }
    }

    private ChatbotRequestDTO createChatRequest(String userMessage) {
        return new ChatbotRequestDTO(
                "gpt-3.5-turbo", // 사용할 모델
                List.of(new MessageDTO("user", userMessage)) // 사용자 메시지를 포함한 요청
        );
    }

}
