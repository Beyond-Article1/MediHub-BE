package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import mediHub_be.common.aggregate.entity.BaseFullEntity;

@Entity
@Table(name = "cp_opinion")
@Getter
public class CpOpinion extends BaseFullEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpOpinionSeq;              // cp 의견 번호

    @Column
    private long userSeq;                   // 작성자

    @Column
    private long cpOpinionLocationSeq;      // cp 의견 위치 번호

    @Column
    private String cpOpinionContent;        // cp 의견 내용

    @Column
    private long cpOpinionViewCount;        // 조회수
}
