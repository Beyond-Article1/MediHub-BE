package mediHub_be.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminMedicalLifePictureDTO {
    private Long pictureSeq;
    private String pictureName;
    private String pictureUrl;
    private String pictureType;
    private Boolean isDeleted;
}
