package mediHub_be.anonymousBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnonymousBoardDetailDTO {

    private Long anonymousBoardSeq;
    private String userId;
    private String anonymousBoardTitle;
    private String anonymousBoardContent;
    private Long anonymousBoardViewCount;
    private LocalDateTime createdAt;
    private List<AnonymousBoardPictureDTO> anonymousBoardPictureList;
    private List<AnonymousBoardKeywordDTO> keywords;
}