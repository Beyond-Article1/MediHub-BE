package mediHub_be.case_sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateDetailDTO {
    private Long templateSeq;
    private String templateTitle;
    private String templateContent; // 템플릿 내용
    private String openScope; // 공개 범위
    private String partName;        // 파트 이름
}
