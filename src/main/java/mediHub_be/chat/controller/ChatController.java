package mediHub_be.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.chat.dto.ChatMessageDTO;
import mediHub_be.chat.dto.ResponseAttachFileDTO;
import mediHub_be.chat.dto.ResponseChatMessageDTO;
import mediHub_be.chat.service.ChatService;
import mediHub_be.chat.service.KafkaProducerService;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Tag(name = "채팅", description = "채팅 API")
public class ChatController {

    private final ChatService chatService;
    private final KafkaProducerService kafkaProducerService;

    @MessageMapping("/{chatroomSeq}")
//    @SendTo("/subscribe/{chatroomSeq}")
    public ResponseChatMessageDTO sendMessage(@Payload ChatMessageDTO message, @DestinationVariable Long chatroomSeq) {
        log.info("Message sent to room {}: {}", chatroomSeq, message);

        // MongoDB에 message 저장
        ResponseChatMessageDTO savedMessage = chatService.saveMessage(message);
        log.info("SavedMessage: {}", savedMessage);

        // Kafka로 메시지 전송
        kafkaProducerService.sendMessageToKafka(savedMessage);

        return savedMessage;
    }

    @Operation(summary = "첨부파일 업로드", description = "첨부파일 업로드")
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseChatMessageDTO sendFile(
            @RequestPart(value = "message") ChatMessageDTO message, @RequestPart(value = "file") MultipartFile file
    ) {
        log.info("File sent to room {}: {}", message.getChatroomSeq(), file.getOriginalFilename());

        // S3에 파일 저장 및 mongoDB에 message 저장
        ResponseChatMessageDTO savedFileMessage = chatService.saveFileMessage(message, file);

        // Kafka로 메시지 전송
        kafkaProducerService.sendMessageToKafka(savedFileMessage);

        return savedFileMessage;
    }

    @Operation(summary = "채팅 메시지 삭제", description = "채팅 메시지 삭제")
    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable String messageId) {
        chatService.deleteMessage(messageId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(null));
    }

    @Operation(summary = "채팅 메시지 조회", description = "특정 채팅방 내 채팅 메시지 조회")
    @GetMapping("/{chatroomSeq}/messages")
    public ResponseEntity<ApiResponse<List<ResponseChatMessageDTO>>> getMessagesByRoomSeq(@PathVariable Long chatroomSeq) {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        List<ResponseChatMessageDTO> messages = chatService.getMessagesByRoomSeq(userSeq, chatroomSeq);
        return ResponseEntity.ok(ApiResponse.ok(messages));
    }

    @Operation(summary = "첨부파일 목록 조회", description = "사용자별 첨부파일 목록 조회")
    @GetMapping("/files")
    public ResponseEntity<ApiResponse<List<ResponseAttachFileDTO>>> getFilesByUserSeq() {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        List<ResponseAttachFileDTO> files = chatService.getFilesByUserSeq(userSeq);
        return ResponseEntity.ok(ApiResponse.ok(files));
    }

}
