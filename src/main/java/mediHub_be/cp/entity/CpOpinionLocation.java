package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "cp_opinion_location")
@Getter
public class CpOpinionLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpOpinionLocationSeq;      // cp 의견위치 번호

    @Column
    private long cpVersionSeq;              // cp 버전 버호

    @Column
    private double cpOpinionLocationX;      // x 좌표

    @Column
    private double cpOpinionLocationY;      // y 좌표
}
