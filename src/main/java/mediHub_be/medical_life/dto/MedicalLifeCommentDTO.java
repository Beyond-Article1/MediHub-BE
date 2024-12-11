package mediHub_be.medical_life.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MedicalLifeCommentDTO {
    private Long commentSeq;
    private String userName;
    private String commentContent;
    private LocalDateTime createdAt;
}
