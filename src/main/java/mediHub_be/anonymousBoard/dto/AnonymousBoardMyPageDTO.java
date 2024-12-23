package mediHub_be.anonymousBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnonymousBoardMyPageDTO {

    private Long anonymousBoardSeq;
    private String anonymousBoardTitle;
    private Long anonymousBoardViewCount;
    private LocalDateTime createdAt;
}