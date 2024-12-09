package mediHub_be.admin.dto;

import lombok.Data;
import mediHub_be.user.entity.UserAuth;
import mediHub_be.user.entity.UserStatus;

@Data
public class AdminUpdateDTO {
    private Long partSeq;
    private Long rankingSeq;
    private Long pictureSeq;
    private String userEmail;
    private String userPhone;
    private UserAuth userAuth;
    private UserStatus userStatus;
}
