package mediHub_be.case_sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseSharingCommentListDTO {
    private Integer caseSharingCommentStartOffset; // 댓글이 달린 본문 시작 위치
    private Integer caseSharingCommentEndOffset; // 댓글이 달린 본문 끝 위치
}
