package mediHub_be.anonymousBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AnonymousBoardTop3DTO {

    private Long anonymousBoardSeq;
    private String anonymousBoardTitle;
    private String userName;
    private LocalDateTime createdAt;
}