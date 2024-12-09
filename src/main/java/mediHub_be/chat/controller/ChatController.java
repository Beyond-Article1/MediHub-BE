package mediHub_be.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.chat.dto.ChatMessageDTO;
import mediHub_be.chat.service.ChatService;
import mediHub_be.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Tag(name = "채팅", description = "채팅 API")
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/hello")       // 클라이언트가 "/app/hello"로 보낸 메시지를 처리
    @SendTo("/topic/greetings")     // 클라이언트가 메시지를 보내면 "/topic/greetings"로 응답
    public String greeting(String message) {
        return "Hello " + message;
    }

    @MessageMapping("/send/chat/{chatroomSeq}")
    @SendTo("/topic/chat/{chatroomSeq}")
    public ChatMessageDTO sendMessage(@Payload ChatMessageDTO message, @DestinationVariable String chatroomSeq) {
        message.setChatroomSeq(chatroomSeq);
        ChatMessageDTO savedMessage = chatService.saveMessage(message);
        log.info("Message sent to room {}: {}", chatroomSeq, savedMessage);
//        return savedMessage;
        return message;
    }

    @Operation(summary = "채팅 메시지 조회", description = "특정 채팅방 내 채팅 메시지 조회")
    @GetMapping("/groups/{chatroomSeq}")
    public ResponseEntity<ApiResponse<List<ChatMessageDTO>>> getMessagesByRoomSeq(@PathVariable String chatroomSeq) {
        List<ChatMessageDTO> messages = chatService.getMessagesByRoomSeq(chatroomSeq);
        return ResponseEntity.ok(ApiResponse.ok(messages));
    }

}
