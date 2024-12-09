package mediHub_be.admin.dto;

import lombok.Data;
import mediHub_be.user.entity.UserAuth;
import mediHub_be.user.entity.UserStatus;

@Data
public class CreateUserDTO {
    private String userId;
    private String userPassword;
    private String userName;
    private String userEmail;
    private String userPhone;
    private Long partSeq;
    private Long rankingSeq;
    private Long pictureSeq;
    private UserAuth userAuth;
    private UserStatus userStatus;
}

