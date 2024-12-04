package mediHub_be.rank.entity;

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
@Table(name = "rank")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE rank SET del_date = LOCALTIME WHERE rank_seq = ?")
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rank_seq", nullable = false)
    private long rankSeq;

    @Column(name = "dept_seq", nullable = false)
    private long deptSeq;

    @Column(name = "rank_ranking", nullable = false)
    private int rankRanking;

    @Column(name = "rank_name", nullable = false)
    private String rankName;

    @Builder
    public Rank(long deptSeq, int rankRanking, String rankName) {
        this.deptSeq = deptSeq;
        this.rankRanking = rankRanking;
        this.rankName = rankName;
    }

    public void updateRank(long deptSeq, int rankRanking, String rankName) {
        this.deptSeq = deptSeq;
        this.rankRanking = rankRanking;
        this.rankName = rankName;
    }
}
