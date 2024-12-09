package mediHub_be.user.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String userPassword;
    private String userName;
    private String userEmail;
    private String userPhone;
    private long partSeq;
    private long rankingSeq;
}
