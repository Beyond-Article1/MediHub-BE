package mediHub_be.ranking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "ranking")
@EntityListeners(AuditingEntityListener.class)
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ranking_seq", nullable = false)
    private long rankingSeq;

    @Column(name = "dept_seq", nullable = false)
    private long deptSeq;

    @Column(name = "ranking_num", nullable = false)
    private int rankingNum;

    @Column(name = "ranking_name", nullable = false)
    private String rankingName;

    @Builder
    public Ranking(long deptSeq, int rankingNum, String rankingName) {
        this.deptSeq = deptSeq;
        this.rankingNum = rankingNum;
        this.rankingName = rankingName;
    }

    public void updateRanking(long deptSeq, int rankingNum, String rankingName) {
        this.deptSeq = deptSeq;
        this.rankingNum = rankingNum;
        this.rankingName = rankingName;
    }
}

