package mediHub_be.admin.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdminMedicalLifeCommentDTO {
    private Long commentSeq;
    private String userName;
    private String commentContent;
    private LocalDateTime createdAt;
}
