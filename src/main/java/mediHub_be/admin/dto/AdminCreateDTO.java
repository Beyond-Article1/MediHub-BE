package mediHub_be.admin.dto;

import lombok.Data;

@Data
public class AdminCreateDTO {
    private String userId;
    private String userPassword;
    private String userName;
    private String userEmail;
    private String userPhone;
    private long partSeq;
    private long rankingSeq;
}

