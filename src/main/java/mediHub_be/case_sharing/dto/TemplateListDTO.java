package mediHub_be.case_sharing.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TemplateListDTO {
    private Long templateSeq;
    private String templateTitle;
    private String templatePreviewImgUrl;

    private String userName;
    private String authorRankName; //작성자 직위명
    private LocalDateTime createdAt;
}
