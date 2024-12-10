package mediHub_be.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminResponseDTO {
    private String userName;
    private String rankingName;
    private String partName;
    private String userEmail;
    private String userPhone;
    private String profileImage;
}