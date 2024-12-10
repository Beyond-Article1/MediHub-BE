package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;
import mediHub_be.board.entity.Keyword;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ResponseCpOpinionWithKeywordListAndCpOpinionVoteDTO {

    private long cpOpinionSeq;              // CP 의견 번호
    private String cpOpinionContent;        // CP 의견 내용
    private LocalDateTime createdAt;        // CP 의견 생성일
    private LocalDateTime updatedAt;        // CP 의견 수정일
    private LocalDateTime deletedAt;        // CP 의견 삭제일
    private long cpOpinionViewCount;        // CP 의견 조회수
    private String userName;                // CP 의견 작성자명
    private String userId;                  // CP 의견 작성자 아이디
    private String partName;                // CP 의견 과명 (ex: 외과, 내과)
    private List<Keyword> keywordList;      // 키워드 리스트
    private long positiveVotes;             // 찬성표
    private long negativeVotes;             // 반대표
    private double positiveRatio;           // 찬성비율
    private double negativeRatio;           // 반대비율

    public static ResponseCpOpinionWithKeywordListAndCpOpinionVoteDTO create(
            ResponseCpOpinionDTO data,
            List<Keyword> keywordList,
            long positiveVotes,
            long negativeVotes,
            double positiveRatio,
            double negativeRatio) {

        return ResponseCpOpinionWithKeywordListAndCpOpinionVoteDTO.builder()
                .cpOpinionSeq(data.getCpOpinionSeq())
                .cpOpinionContent(data.getCpOpinionContent())
                .createdAt(data.getCreatedAt())
                .updatedAt(data.getUpdatedAt())
                .deletedAt(data.getDeletedAt())
                .cpOpinionViewCount(data.getCpOpinionViewCount())
                .userName(data.getUserName())
                .userId(data.getUserId())
                .partName(data.getPartName())
                .keywordList(keywordList)
                .positiveVotes(positiveVotes)
                .negativeVotes(negativeVotes)
                .positiveRatio(positiveRatio)
                .negativeRatio(negativeRatio)
                .build();
    }

    public static ResponseCpOpinionWithKeywordListAndCpOpinionVoteDTO calculateVoteRatioAndCreate(
            ResponseCpOpinionDTO data,
            List<Keyword> keywordList,
            List<CpOpinionVoteDTO> votes) {
        int positiveVotes = 0;
        int negativeVotes = 0;
        double positiveRatio;
        double negativeRatio;

        for (CpOpinionVoteDTO vote : votes) {
            if (vote.isCpOpinionVote()) {
                positiveVotes++;
            } else {
                negativeVotes++;
            }
        }

        long totalVotes = positiveVotes + negativeVotes;

        positiveRatio = (double) positiveVotes / totalVotes * 100;
        negativeRatio = (double) negativeVotes / totalVotes * 100;

        return ResponseCpOpinionWithKeywordListAndCpOpinionVoteDTO.create(data, keywordList, positiveVotes, negativeVotes, positiveRatio, negativeRatio);
    }
}
