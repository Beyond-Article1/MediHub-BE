package mediHub_be.medical_life.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalLifeListDTO {
    private Long medicalLifeSeq;
    private Long userSeq;
    private String userName;
    private String PartSeq;
    private String DeptSeq;
    private String medicalLifeName;
    private String medicalLifeTitle;
    private String medicalLifeContent;
    private Boolean medicalLifeIsDeleted;
    private Long medicalLifeViewCount;
}
