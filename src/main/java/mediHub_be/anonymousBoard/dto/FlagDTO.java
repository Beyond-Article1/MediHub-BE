package mediHub_be.anonymousBoard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlagDTO {

    private Long flagSeq;
    private String flagType;
    private Long flagentitySeq;
}