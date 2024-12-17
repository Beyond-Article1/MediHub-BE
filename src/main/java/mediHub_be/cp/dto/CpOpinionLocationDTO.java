package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;
import mediHub_be.cp.entity.CpOpinionLocation;


@Data
public class CpOpinionLocationDTO {

    private long cpOpinionLocationSeq;      // cp 의견 위치 번호
    private long cpVersionSeq;              // cp 버전 번호
    private long cpOpinionLocationPageNum;  // cp 의견 위치 페이지 번호
    private double cpOpinionLocationX;      // x 좌표
    private double cpOpinionLocationY;      // y 좌표

    @Builder
    public CpOpinionLocationDTO(
            long cpOpinionLocationSeq,
            long cpVersionSeq,
            long cpOpinionLocationPageNum,
            double cpOpinionLocationX,
            double cpOpinionLocationY) {
        this.cpOpinionLocationSeq = cpOpinionLocationSeq;
        this.cpVersionSeq = cpVersionSeq;
        this.cpOpinionLocationPageNum = cpOpinionLocationPageNum;
        this.cpOpinionLocationX = cpOpinionLocationX;
        this.cpOpinionLocationY = cpOpinionLocationY;
    }

    public static CpOpinionLocationDTO toDto(CpOpinionLocation entity) {
        return CpOpinionLocationDTO.builder()
                .cpOpinionLocationSeq(entity.getCpOpinionLocationSeq())
                .cpVersionSeq(entity.getCpVersionSeq())
                .cpOpinionLocationPageNum(entity.getCpOpinionLocationPageNum())
                .cpOpinionLocationX(entity.getCpOpinionLocationX())
                .cpOpinionLocationY(entity.getCpOpinionLocationY())
                .build();
    }
}
