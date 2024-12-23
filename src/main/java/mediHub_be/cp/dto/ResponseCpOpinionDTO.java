package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mediHub_be.board.entity.Keyword;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ResponseCpOpinionDTO {

    private long cpOpinionSeq;              // CP 의견 번호
    private String cpOpinionContent;        // CP 의견 내용
    private LocalDateTime createdAt;        // CP 의견 생성일
    private LocalDateTime updatedAt;        // CP 의견 수정일
    private LocalDateTime deletedAt;        // CP 의견 삭제일
    private long cpOpinionViewCount;        // CP 의견 조회수
    private String userName;                // CP 의견 작성자명
    private String userId;                  // CP 의견 작성자 아이디
    private long userSeq;                   // CP 의견 작성자 번호
    private String partName;                // CP 의견 과명 (ex: 외과, 내과)
    private String profileUrl;              // 작성자 프로필 사진
    private List<Keyword> keywordList;      // 키워드 리스트
    private long positiveVotes;             // 찬성표
    private long negativeVotes;             // 반대표
    private double positiveRatio;           // 찬성비율
    private double negativeRatio;           // 반대비율

    @Builder
    public ResponseCpOpinionDTO(
            long cpOpinionSeq,
            String cpOpinionContent,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt,
            long cpOpinionViewCount,
            String userName,
            String userId,
            long userSeq,
            String partName) {
        this.cpOpinionSeq = cpOpinionSeq;
        this.cpOpinionContent = cpOpinionContent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.cpOpinionViewCount = cpOpinionViewCount;
        this.userName = userName;
        this.userId = userId;
        this.userSeq = userSeq;
        this.partName = partName;
    }

    public static ResponseCpOpinionDTO create(ResponseCpOpinionDTO origin, List<Keyword> keywordList) {
        origin.setKeywordList(keywordList);
        origin.setZero();

        return origin;
    }

    public static ResponseCpOpinionDTO create(
            ResponseCpOpinionDTO origin,
            List<Keyword> keywordList,
            List<CpOpinionVoteDTO> cpOpinionVoteDTOList) {
        long positiveVotes = 0L;
        long negativeVotes = 0L;
        double positiveRatio;
        double negativeRatio;

        for (CpOpinionVoteDTO vote : cpOpinionVoteDTOList) {
            if (vote.isCpOpinionVote()) {
                positiveVotes++;
            } else {
                negativeVotes++;
            }
        }

        long totalVotes = positiveVotes + negativeVotes;

        positiveRatio = (double) positiveVotes / totalVotes * 100;
        negativeRatio = (double) negativeVotes / totalVotes * 100;

        ResponseCpOpinionDTO dto = ResponseCpOpinionDTO.create(origin, keywordList);
        dto.setPositiveVotes(positiveVotes);
        dto.setNegativeVotes(negativeVotes);
        dto.setPositiveRatio(positiveRatio);
        dto.setNegativeRatio(negativeRatio);

        return dto;
    }

    private void setZero() {
        this.positiveVotes = 0L;
        this.negativeVotes = 0L;
        this.positiveRatio = 0.0;
        this.negativeRatio = 0.0;
    }
}
