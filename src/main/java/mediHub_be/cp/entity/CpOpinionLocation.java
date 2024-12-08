package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mediHub_be.common.aggregate.entity.BaseCreateDeleteEntity;
import mediHub_be.cp.dto.RequestCpOpinionLocationDTO;

@Entity
@Table(name = "cp_opinion_location")
@Getter
@NoArgsConstructor
public class CpOpinionLocation extends BaseCreateDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpOpinionLocationSeq;      // cp 의견위치 번호

    @Column
    private long cpVersionSeq;              // cp 버전 번호

    @Column(name = "cp_opinion_location_x")
    private double cpOpinionLocationX;      // x 좌표

    @Column(name = "cp_opinion_location_y")
    private double cpOpinionLocationY;      // y 좌표

    @Builder
    public CpOpinionLocation(
            long cpVersionSeq,
            double cpOpinionLocationX,
            double cpOpinionLocationY) {
    }

    public static CpOpinionLocation create(long cpVersionSeq, RequestCpOpinionLocationDTO requestBody) {
        return CpOpinionLocation.builder()
                .cpVersionSeq(cpVersionSeq)
                .cpOpinionLocationX(requestBody.getCpOpinionLocationX())
                .cpOpinionLocationY(requestBody.getCpOpinionLocationY())
                .build();
    }
}
