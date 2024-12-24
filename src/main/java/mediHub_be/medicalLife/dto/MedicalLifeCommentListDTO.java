package mediHub_be.medicalLife.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalLifeCommentListDTO {

    private Long commentSeq;
    private Long userSeq;
    private String userName;
    private String part;
    private String rankingName;
    private String commentContent;
    private LocalDateTime createdAt;
}
