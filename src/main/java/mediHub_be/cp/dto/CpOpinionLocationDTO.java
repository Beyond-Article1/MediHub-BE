package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;
import mediHub_be.cp.entity.CpOpinionLocation;

@Data
@Builder
public class CpOpinionLocationDTO {

    private Long cpOpinionLocationSeq;      // cp 의견위치 번호
    private long cpVersionSeq;              // cp 버전 번호
    private double cpOpinionLocationX;      // x 좌표
    private double cpOpinionLocationY;      // y 좌표

    public static CpOpinionLocationDTO toDto(CpOpinionLocation entity) {
        return CpOpinionLocationDTO.builder()
                .cpOpinionLocationSeq(entity.getCpOpinionLocationSeq())
                .cpVersionSeq(entity.getCpVersionSeq())
                .cpOpinionLocationX(entity.getCpOpinionLocationX())
                .cpOpinionLocationY(entity.getCpOpinionLocationY())
                .build();
    }
}
