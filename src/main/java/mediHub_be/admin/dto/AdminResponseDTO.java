package mediHub_be.admin.dto;

import lombok.Data;

@Data
public class AdminResponseDTO {
    private Long userSeq;
    private Long partSeq;
    private Long rankingSeq;
    private Long pictureSeq;
    private String userName;
    private String userId;
    private String userEmail;
    private String userPhone;
    private String userAuth;
    private String userStatus;
    private String createdAt;
}
