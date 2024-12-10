package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;
import mediHub_be.cp.entity.CpOpinionVote;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CpOpinionVoteDTO {

    private long cpOpinionVoteSeq;      // cp 의견투표 번호
    private long cpOpinionSeq;          // cp 의견 번호
    private long userSeq;               // 투표자 번호
    private boolean cpOpinionVote;      // cp 의견투표 내용
    private LocalDateTime createdAt;    // 생성일

    public static CpOpinionVoteDTO toDto(CpOpinionVote entity) {

        return CpOpinionVoteDTO.builder()
                .cpOpinionVoteSeq(entity.getCpOpinionVoteSeq())
                .cpOpinionSeq(entity.getCpOpinionSeq())
                .userSeq(entity.getUserSeq())
                .cpOpinionVote(entity.isCpOpinionVote())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
