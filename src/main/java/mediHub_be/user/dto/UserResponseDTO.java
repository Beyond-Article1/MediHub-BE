package mediHub_be.user.dto;

import lombok.Builder;
import lombok.Data;
import mediHub_be.dept.entity.Dept;

@Data
@Builder
public class UserResponseDTO {
    private String userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String rankingName;
    private String partName;
    private String deptName;
    private String profileImage;
}
