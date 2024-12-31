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
    private Long deptSeq;
    private String deptName;
    private Long partSeq;
    private String partName;
    private String medicalLifeTitle;
    private String medicalLifeContent;
    private Boolean medicalLifeIsDeleted;
    private Long medicalLifeViewCount;
    private LocalDateTime createdAt;
    private List<String> keywords;
    private String rankingName;
}
