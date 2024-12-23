package mediHub_be.medicalLife.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private LocalDateTime createdAt;
    private List<String> keywords;
    private String rankingName;
}
