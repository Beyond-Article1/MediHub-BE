package mediHub_be.user.dto;

import lombok.Data;

@Data
public class UserUpdateRequestDTO {
    private String userEmail;
    private String userPhone;
    private String userPassword;
}
