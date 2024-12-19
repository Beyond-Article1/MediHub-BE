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
    private String content; // 댓글 내용
    private String blockId; // 댓글이 다린 케이스 공유글 block id
}
