package mediHub_be.medicalLife.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MedicalLifeBookMarkDTO {
    private Long medicalLifeSeq;
    private Long userSeq;
    private String userName;
    private String partName;
    private String medicalLifeName;
    private String medicalLifeTitle;
    private String medicalLifeContent;
    private Long medicalLifeViewCount;
    private LocalDateTime createdAt;
}
