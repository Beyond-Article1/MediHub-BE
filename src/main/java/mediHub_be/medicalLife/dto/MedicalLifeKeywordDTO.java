package mediHub_be.medicalLife.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalLifeKeywordDTO {
    private Long keywordSeq;
    private Long flagSeq;
    private String keywordName;
}
