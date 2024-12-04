package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import mediHub_be.common.aggregate.entity.BaseCreateDeleteEntity;

@Entity
@Table(name = "cp_opinion_vote")
@Getter
public class CpOpinionVote extends BaseCreateDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpOpinionVoteSeq;      // cp 의견투표 번호

    @Column
    private long cpOpinionSeq;          // cp 의견 번호

    @Column
    private boolean cpOpinionVote;      // cp 의견투표 내용
}
