package mediHub_be.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mediHub_be.user.entity.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFollowingsDTO {
    private Long followSeq;
    private Long userSeq;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String partName;
    private String rankingName;

    // 팔로잉 목록 확인 생성
    public ResponseFollowingsDTO(Long followSeq, User user) {
        this.followSeq = followSeq;
        this.userSeq = user.getUserSeq();
        this.userName = user.getUserName();
        this.userEmail = user.getUserEmail();
        this.userPhone = user.getUserPhone();
        this.partName = user.getPart().getPartName();
        this.rankingName = user.getRanking().getRankingName();
    }
}
