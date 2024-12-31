package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCpOpinionLocationDTO {

    private double cpOpinionLocationX;
    private double cpOpinionLocationY;
    private long cpOpinionLocationPageNum;
}
