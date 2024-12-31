package mediHub_be.admin.dto;

import lombok.Builder;
import lombok.Data;
import mediHub_be.user.entity.UserAuth;
import mediHub_be.user.entity.UserStatus;

@Data
@Builder
public class AdminUserDetailResponseDTO {
    private long userSeq;
    private String userName;
    private String userId;
    private String userEmail;
    private String userPhone;
    private UserAuth userAuth;
    private UserStatus userStatus;
    private long rankingSeq;
    private String rankingName;
    private long partSeq;
    private String partName;
    private long deptSeq;
    private String profileImage;
}
