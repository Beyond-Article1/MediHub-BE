package mediHub_be.ranking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RankingDTO {
    private long rankingSeq;
    private long deptSeq;
    private int rankingNum;
    private String rankingName;
}
