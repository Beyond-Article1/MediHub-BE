package mediHub_be.case_sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseSharingCommentRequestDTO {
    private Long caseSharingSeq; // 댓글이 달린 케이스 공유글 ID
    private String content; // 댓글 내용
    private Integer startOffset; // 본문 시작 위치
    private Integer endOffset; // 본문 끝 위치
}
