package mediHub_be.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mediHub_be.chat.entity.ChatMessage;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseAttachFileDTO {
    private String messageSeq;  // 메시지 고유번호(_id)
    private Long senderUserSeq;
    private String senderUserName;
    private String rankingName;
    private String type;
    private String createdAt;
    private ChatMessage.Attachment attachment;
}
