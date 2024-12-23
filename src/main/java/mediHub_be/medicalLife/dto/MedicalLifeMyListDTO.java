package mediHub_be.medicalLife.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalLifeMyListDTO {

    private Long medicalLifeSeq;
    private String medicalLifeTitle;
    private Long medicalLifeViewCount;
    private LocalDateTime createdAt;
}
