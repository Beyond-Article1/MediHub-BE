package mediHub_be.medicalLife.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalLifeMyListDTO {

    private Long medicalLifeSeq;
    private String medicalLifeTitle;
    private String medicalLifeContent;
    private Long medicalLifeViewCount;
    private List<String> keywords;
    private String userName;
    private LocalDateTime createdAt;
}
