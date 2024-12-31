package mediHub_be.case_sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class CaseSharingCommentDetailDTO {

    private Long commentSeq;
    private String blockId;
    private String userId;
    private String userName; // 댓글 작성자명
    private String userRankName; //댓글 작성자 직위명
    private String content; // 댓글 내용
    private LocalDateTime createdAt; // 작성일
    private String userProfileURL; // 댓글 작성자 이미지 url

}

