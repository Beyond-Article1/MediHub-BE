package mediHub_be.anonymousBoard.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnonymousBoardPictureDTO {

    private Long pictureSeq;
    private Long flagSeq;
    private String pictureName;
    private String pictureChangedName;
    private String pictureUrl;
    private String pictureType;
    private Boolean pictureIsDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}