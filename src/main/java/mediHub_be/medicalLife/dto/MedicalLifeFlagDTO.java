package mediHub_be.medicalLife.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MedicalLifeFlagDTO {
    private Long flagSeq;
    private String flagType;
    private Long flagEntitySeq;
}
