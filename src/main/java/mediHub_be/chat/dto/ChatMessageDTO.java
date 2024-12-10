package mediHub_be.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
    private Long chatroomSeq;
    private Long senderUserSeq;
    private String type;                // 메시지 타입(text, file, image 등)
    private String message;
    private LocalDateTime createdAt;
    private List<String> attachments;   // 첨부파일 url
}
