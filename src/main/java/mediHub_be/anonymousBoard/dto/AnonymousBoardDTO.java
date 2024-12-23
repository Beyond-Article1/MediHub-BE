package mediHub_be.anonymousBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import mediHub_be.board.entity.Keyword;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class AnonymousBoardDTO {

    private Long anonymousBoardSeq;
    private String userName;
    private String anonymousBoardTitle;
    private Long anonymousBoardViewCount;
    private LocalDateTime createdAt;
    private List<Keyword> keywordList;
}