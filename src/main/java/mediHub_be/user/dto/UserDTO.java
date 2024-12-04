package mediHub_be.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import mediHub_be.user.entity.UserAuth;
import mediHub_be.user.entity.UserStatus;

@Getter
@Setter
@Builder
public class UserDTO {
    private String userId;
    private String userPassword;
    private String userName;
    private String userEmail;
    private String userPhone;
    private UserAuth userAuth;
    private UserStatus userStatus;
}
