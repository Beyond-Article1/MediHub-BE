package mediHub_be.admin.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AdminMedicalLifeDetailDTO {
    private Long medicalLifeSeq;
    private String userName;
    private String deptName;
    private String partName;
    private String medicalLifeTitle;
    private String medicalLifeContent;
    private Long medicalLifeViewCount;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private List<AdminMedicalLifePictureDTO> pictures;
}
