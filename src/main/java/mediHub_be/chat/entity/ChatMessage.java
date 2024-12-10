package mediHub_be.chat.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mediHub_be.common.utils.DateTimeUtil;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "chat.messages")
@Getter
@ToString
@NoArgsConstructor
public class ChatMessage {

    @Id
    private String id;
    private Long chatroomSeq;
    private Long senderUserSeq;
    private String type;                // 메시지 타입(text, file, image 등)
    private String message;
    private LocalDateTime createdAt;
    private boolean isDeleted;          // 메시지 삭제 여부
    private List<String> attachments;   // 첨부파일 url

    @Builder
    public ChatMessage(Long chatroomSeq, Long senderUserSeq, String type, String message, LocalDateTime createdAt, boolean isDeleted, List<String> attachments) {
        this.chatroomSeq = chatroomSeq;
        this.senderUserSeq = senderUserSeq;
        this.type = type;
        this.message = message;
        this.createdAt = DateTimeUtil.localDateTimeToLocalDateTime(LocalDateTime.now());
        this.isDeleted = isDeleted;
        this.attachments = attachments;
    }

    public void setIsDeleted(boolean b) {
        this.isDeleted = b;
    }
}
