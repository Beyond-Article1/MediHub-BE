package mediHub_be.rank.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RankDTO {
    private long rankSeq;
    private long deptSeq;
    private int rankRanking;
    private String rankName;
}

