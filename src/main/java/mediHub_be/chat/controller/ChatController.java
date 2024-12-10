package mediHub_be.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.chat.dto.ChatMessageDTO;
import mediHub_be.chat.dto.ResponseChatMessageDTO;
import mediHub_be.chat.service.ChatService;
import mediHub_be.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

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
    public ResponseChatMessageDTO sendMessage(@Payload ChatMessageDTO message, @DestinationVariable Long chatroomSeq) {
//        Long userSeq = SecurityUtil.getCurrentUserSeq();
//        log.info("메시지 보낸 사람의 userSeq 확인 : {}", userSeq);
        log.info("Message sent to room {}: {}", chatroomSeq, message);
        message.setChatroomSeq(chatroomSeq);
        ResponseChatMessageDTO savedMessage = chatService.saveMessage(message);
        log.info("SavedMessage: {}", savedMessage);
        return savedMessage;
    }

    @Operation(summary = "채팅 메시지 삭제", description = "채팅 메시지 삭제")
    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable String messageId) {
        chatService.deleteMessage(messageId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(null));
    }

    @Operation(summary = "채팅 메시지 조회", description = "특정 채팅방 내 채팅 메시지 조회")
    @GetMapping("/room/{chatroomSeq}")
    public ResponseEntity<ApiResponse<List<ResponseChatMessageDTO>>> getMessagesByRoomSeq(@PathVariable Long chatroomSeq) {
        List<ResponseChatMessageDTO> messages = chatService.getMessagesByRoomSeq(chatroomSeq);
        return ResponseEntity.ok(ApiResponse.ok(messages));
    }

}
