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
public class CaseSharingDraftUpdateDTO {
    private String caseSharingTitle; // 제목
    private String caseSharingContent; // 본문 내용
    private List<String> keywords; // 키워드 리스트
}
