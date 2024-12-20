package mediHub_be.user.dto;

import lombok.Data;

@Data
public class UserSearchDTO {
    private Long userSeq;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String rankingName;
    private String partName;
    private String profileImage;
}
