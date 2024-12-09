package mediHub_be.chat.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "chat.messages")
@Getter
@NoArgsConstructor
public class ChatMessage {

    @Id
    private String messageSeq;
    private String chatroomSeq;
    private String senderName;
    private LocalDateTime createdAt;
    private String message;
    private String type;                // 메시지 타입(text, file, image 등)
    private boolean isDeleted;          // 메시지 삭제 여부
    private List<String> attachments;   // 첨부파일 url

}
