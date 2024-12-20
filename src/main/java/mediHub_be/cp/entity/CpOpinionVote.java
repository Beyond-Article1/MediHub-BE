package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mediHub_be.common.aggregate.entity.CreateTimeEntity;

@Entity
@Table(name = "cp_opinion_vote")
@Getter
@NoArgsConstructor
public class CpOpinionVote extends CreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpOpinionVoteSeq;      // cp 의견투표 번호

    @Column
    private long cpOpinionSeq;          // cp 의견 번호

    @Column
    private long userSeq;               // 투표자 번호

    @Column
    private boolean cpOpinionVote;      // cp 의견투표 내용

    @Builder
    public CpOpinionVote(
            long cpOpinionSeq,
            long userSeq,
            boolean cpOpinionVote) {
        this.cpOpinionSeq = cpOpinionSeq;
        this.userSeq = userSeq;
        this.cpOpinionVote = cpOpinionVote;
    }

    public static CpOpinionVote toEntity(
            long cpOpinionSeq,
            long userSeq,
            boolean cpOpinionVote) {

        return CpOpinionVote.builder()
                .cpOpinionSeq(cpOpinionSeq)
                .userSeq(userSeq)
                .cpOpinionVote(cpOpinionVote)
                .build();
    }
}
