package mediHub_be.case_sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateListDTO {
    private Long templateSeq;
    private String templateTitle;
    private String previewImageUrl; // 미리보기 이미지 URL
}
