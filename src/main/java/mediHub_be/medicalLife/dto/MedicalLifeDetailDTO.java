package mediHub_be.medicalLife.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalLifeDetailDTO {

    private Long userSeq;
    private String userName;
    private String rankingName;
    private String medicalLifeTitle;
    private String medicalLifeContent;
    private String medicalLifeViewCount;
    private LocalDateTime createdAt;
    private List<MedicalLifeKeywordDTO> keywords;
    private String profileImage;
}
