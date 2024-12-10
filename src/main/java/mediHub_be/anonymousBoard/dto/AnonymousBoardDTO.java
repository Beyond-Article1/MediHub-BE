package mediHub_be.anonymousBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnonymousBoardDTO {

    private Long anonymousBoardSeq;
    private String userId;
    private String anonymousBoardTitle;
    private Long anonymousBoardViewCount;
    private LocalDateTime createdAt;
}