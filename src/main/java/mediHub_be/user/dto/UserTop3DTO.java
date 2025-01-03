package mediHub_be.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTop3DTO {
    private String userName;
    private String rankingName;
    private String partName;
    private Long likeNum;
    private String profileUrl;
    private Long bookmarkNum;
    private Long totalScore;

}
