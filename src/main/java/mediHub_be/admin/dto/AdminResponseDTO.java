package mediHub_be.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminResponseDTO {
    private Long userSeq;
    private String userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String rankingName;
    private String partName;
    private String deptName;
    private String profileImage;
    private String userStatus;
}