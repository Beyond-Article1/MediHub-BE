package mediHub_be.admin.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminMedicalLifeDTO {
    private Long medicalLifeSeq;
    private String userName;
    private String deptName;
    private String partName;
    private String medicalLifeTitle;
    private String medicalLifeContent;
    private Long medicalLifeViewCount;
    private Boolean isDeleted;
    private List<AdminMedicalLifePictureDTO> pictures;
}
