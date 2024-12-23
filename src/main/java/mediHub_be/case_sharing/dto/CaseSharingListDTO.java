package mediHub_be.case_sharing.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CaseSharingListDTO {
    private Long caseSharingSeq; // seq값
    private String caseSharingTitle; //제목
    private String caseAuthor; // 작성자
    private String caseAuthorRankName; //작성자 직위명
    private LocalDateTime regDate; //작성 일자
    private Long caseSharingViewCount; //조회수


}
