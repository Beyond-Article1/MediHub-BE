package mediHub_be.medical_life.dto;


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
    private String keywordName;
}
