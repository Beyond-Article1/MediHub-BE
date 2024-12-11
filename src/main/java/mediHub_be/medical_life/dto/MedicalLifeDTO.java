package mediHub_be.medical_life.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MedicalLifeDTO {
    private Long medicalLifeSeq;
    private String userName;
    private String deptName;
    private String partName;
    private String medicalLifeTitle;
    private String medicalLifeContent;
    private Boolean medicalLifeIsDeleted;
    private Long medicalLifeViewCount;
}
