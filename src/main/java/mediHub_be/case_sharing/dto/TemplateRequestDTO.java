package mediHub_be.case_sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateRequestDTO {
    private String templateTitle;
    private String templateContent;
    private String openScope;
    private String previewImageUrl;
}
