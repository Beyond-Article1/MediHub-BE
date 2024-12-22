package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseCpVersionDTO {
    private long cpVersionSeq;
    private String cpVersion;
}
