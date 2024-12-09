package mediHub_be.admin.dto;

import lombok.Data;

@Data
public class UserCreateDTO {
    private Long userSeq;
    private Long partSeq;
    private Long rankingSeq;
    private String userName;
    private String userId;
    private String userPassword;
    private String userEmail;
    private String userPhone;
    private String userAuth;
    private String userStatus;

    private String pictureUrl;
    private String pictureName;
    private String pictureType;
}
