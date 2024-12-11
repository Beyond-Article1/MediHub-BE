package mediHub_be.medical_life.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MedicalLifePictureDTO {
    private Long pictureSeq;
    private String pictureName;
    private String pictureUrl;
    private String pictureType;
    private Boolean pictureIsDeleted;
}
