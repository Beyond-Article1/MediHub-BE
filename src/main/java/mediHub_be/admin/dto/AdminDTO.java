package mediHub_be.admin.dto;

import lombok.Data;
import mediHub_be.part.entity.Part;
import mediHub_be.ranking.entity.Ranking;

@Data
public class AdminDTO {
    private String userId;
    private String userPassword;
    private String userName;
    private String userEmail;
    private String userPhone;
    private Part partSeq;
    private Ranking rankingSeq;
}

