package mediHub_be.medicalLife.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MedicalLifeTop3DTO {
    private Long medicalLifeSeq;
    private String medicalLifeTitle;
    private String userName;
    private String partName;
    private String rankingName;
    private LocalDateTime createdAt;
}
