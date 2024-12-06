package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;
import mediHub_be.board.entity.Keyword;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ResponseCpOpinionDTO {

    private long cpOpinionSeq;              // CP 의견 번호
    private String cpOpinionContent;        // CP 의견 내용
    private LocalDateTime createdAt;        // CP 의견 생성일
    private LocalDateTime updatedAt;        // CP 의견 수정일
    private LocalDateTime deletedAt;        // CP 의견 삭제일
    private long cpOpinionViewCount;        // CP 의견 조회수
    private String userName;                // CP 의견 작성자명
    private String userId;                  // CP 의견 작성자 아이디
    private String partName;                // CP 의견 과명 (ex: 외과, 내과)
//    private List<Keyword> keywordList;      // 키워드 리스트
}
