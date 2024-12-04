package mediHub_be.case_sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseSharingKeywordDTO {
    private Long keywordSeq;      // 키워드 ID
    private String keywordName;   // 키워드 이름
}
