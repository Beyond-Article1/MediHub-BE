package mediHub_be.admin.dto;

import lombok.Data;
import mediHub_be.user.entity.UserAuth;
import mediHub_be.user.entity.UserStatus;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AdminUpdateDTO {
    private Long userSeq;
    private Long partSeq;
    private Long rankingSeq;
    private String userEmail;
    private String userPhone;
    private UserAuth userAuth;
    private UserStatus userStatus;
}
