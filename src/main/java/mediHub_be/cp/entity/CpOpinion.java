package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import mediHub_be.common.aggregate.entity.BaseFullEntity;
import mediHub_be.user.entity.User;

@Entity
@Table(name = "cp_opinion")
@Getter
public class CpOpinion extends BaseFullEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpOpinionSeq;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private User userSeq;

    @ManyToOne
    @JoinColumn(name = "cp_opinion_location_seq")
    private CpOpinionLocation cpOpinionLocationSeq;

    @Column
    private String cpOpinionContent;
}
