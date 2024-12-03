package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import mediHub_be.common.aggregate.entity.BaseFullEntity;

@Entity
@Table(name = "CpOpinion")
@Getter
public class CpOpinion extends BaseFullEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpOpinionSeq;

    @Column
    private long userSeq;

    @Column
    private long cpOpinionLocationSeq;

    @Column
    private String cpOpinionContent;
}
