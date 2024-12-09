package mediHub_be.anonymousBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnonymousBoardListDTO {

    private Long anonymousBoardSeq;
    private String userId;
    private String anonymousBoardTitle;
    private Long anonymousBoardViewCount;
    private LocalDateTime createdAt;
}