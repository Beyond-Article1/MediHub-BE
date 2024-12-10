package mediHub_be.user.dto;

import lombok.Data;

@Data
public class UserDTO {
    private long userSeq;
    private String userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private long partSeq;
    private long rankingSeq;
}
