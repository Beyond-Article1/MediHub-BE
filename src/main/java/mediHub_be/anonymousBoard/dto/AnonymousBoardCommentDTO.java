package mediHub_be.anonymousBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnonymousBoardCommentDTO {

    private String userName;
    private String commentContent;
    private LocalDateTime createdAt;
}