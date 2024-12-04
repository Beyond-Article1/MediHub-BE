package mediHub_be.part.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PartDTO {
    private long partSeq;
    private long deptSeq;
    private String partName;
}
