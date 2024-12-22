package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseCpOpinionLocationDTO {

    private long cpOpinionLocationSeq;      // cp 의견위치 번호
    private long cpVersionSeq;              // cp 버전 번호
    private long cpOpinionLocationPageNum;  // cp 의견 위치 페이지 번호
    private double cpOpinionLocationX;      // x 좌표
    private double cpOpinionLocationY;      // y 좌표
    private long userSeq;                   // 작성자
}
