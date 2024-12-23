package mediHub_be.anonymousBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mediHub_be.board.entity.Keyword;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnonymousBoardDetailDTO {

    private String userName;
    private String anonymousBoardTitle;
    private String anonymousBoardContent;
    private Long anonymousBoardViewCount;
    private LocalDateTime createdAt;
    private List<Keyword> keywordList;
}