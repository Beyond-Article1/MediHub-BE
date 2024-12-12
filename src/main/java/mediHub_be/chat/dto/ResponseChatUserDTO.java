package mediHub_be.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseChatUserDTO {
    private Long userSeq;
    private String userName;    // 직원 이름
    private String userProfileUrl;  // 직원 프로필 사진
    private String partName;    // 과명
    private String rankingName; // 직급명
}
