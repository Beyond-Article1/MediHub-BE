package mediHub_be.case_sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CaseSharingMain3DTO {
    private Long caseSharingSeq; // seq값
    private String caseSharingTitle; //제목
    private String caseAuthor; // 작성자
    private String partName; // 소속 part
    private String caseAuthorRankName; //작성자 직위명
    private String firstPictureUrl; // 대표 사진

}
