package mediHub_be.medicalLife.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalLifeCreateRequestDTO {

    private String medicalLifeTitle;
    private String medicalLifeContent;
    private List<String> keywords;
}
