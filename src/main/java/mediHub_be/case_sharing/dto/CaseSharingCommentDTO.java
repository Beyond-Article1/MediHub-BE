package mediHub_be.case_sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseSharingCommentDTO {
    private Long commentSeq; // 댓글 ID
    private String userName; // 댓글 작성자명
    private String userRankName; //댓글 작성자 직위명
    private String content; // 댓글 내용
    private Integer startOffset; // 본문 시작 위치
    private Integer endOffset; // 본문 끝 위치
    private LocalDateTime createdAt; // 작성일

}

