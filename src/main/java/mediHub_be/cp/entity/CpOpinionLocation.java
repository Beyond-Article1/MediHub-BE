package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import mediHub_be.common.aggregate.entity.BaseCreateDeleteEntity;

@Entity
@Table(name = "cp_opinion_location")
@Getter
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

//    public static CpOpinionLocation create()
}
