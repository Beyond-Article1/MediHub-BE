package mediHub_be.case_sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseSharingDraftDetailDTO {
    private Long caseSharingSeq; // 게시글 ID
    private String caseSharingTitle; // 제목
    private String caseSharingContent; // 본문 내용 (HTML)

    private List<CaseSharingKeywordDTO> keywords; // 키워드 리스트

    private Long caseSharingGroupSeq; // 그룹 ID (CaseSharingGroup 정보)
}
