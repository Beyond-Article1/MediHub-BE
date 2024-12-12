package mediHub_be.chatbot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.chatbot.dto.MessageDTO;
import mediHub_be.chatbot.service.ChatbotWebSocketService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatbotWebSocketController {
    private final ChatbotWebSocketService chatbotService;

    @MessageMapping("/medical-bot")
    @SendToUser("/queue/medical")
    public MessageDTO handleChatMessage(MessageDTO userMessage) {
        log.info("handleChatMessage 호출됨");
        if (userMessage == null) {
            log.error("클라이언트 메시지가 null입니다.");
            return new MessageDTO("bot", "오류가 발생했습니다.");
        }
        log.info("클라이언트 메시지 수신: {}", userMessage);

        String botResponse = chatbotService.getChatbotResponse(userMessage.getContent());
        log.info("챗봇 응답 생성: {}", botResponse);

        return new MessageDTO("bot", botResponse);
    }

}