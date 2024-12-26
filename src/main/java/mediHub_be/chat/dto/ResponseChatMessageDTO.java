package mediHub_be.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mediHub_be.chat.entity.ChatMessage;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseChatMessageDTO {
    private String messageSeq;  // 메시지 고유번호(_id)
    private Long chatroomSeq;
    private Long senderUserSeq;
    private String senderUserName;
    private String senderUserProfileUrl;
    private String type;
    private String message;
    private LocalDateTime createdAt;
    private ChatMessage.Attachment attachment;
}
