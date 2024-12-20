package mediHub_be.medical_life.dto;

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

    private String userName;
    private String commentContent;
    private LocalDateTime createdAt;
}
