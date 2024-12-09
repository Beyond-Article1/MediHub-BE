package mediHub_be.anonymousBoard.dto;

import lombok.Data;

@Data
public class RequestPicture {

    private Long flagSeq;
    private String pictureName;
    private String pictureChangedName;
    private String pictureUrl;
    private String pictureType;
}