package mediHub_be.chatbot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.chatbot.dto.MessageDTO;
import mediHub_be.chatbot.service.ChatbotService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatbotController {
    private final ChatbotService chatbotService;

    @MessageMapping("/medical-bot")  // 클라이언트에서 "/app/medical-bot"으로 메시지 전송
    @SendTo("/topic/medical")       // 클라이언트가 "/topic/medical" 구독 중인 모든 사용자에게 메시지 전송
    public MessageDTO handleChatMessage(MessageDTO userMessage) {
        log.info("클라이언트 메시지 수신: {}", userMessage);

        // ChatGPT API를 통해 응답 생성
        String botResponse = chatbotService.getChatbotResponse(userMessage.getContent());

        log.info("챗봇 응답 생성: {}", botResponse);

        // 챗봇 응답 반환
        return new MessageDTO("bot", botResponse);
    }


}