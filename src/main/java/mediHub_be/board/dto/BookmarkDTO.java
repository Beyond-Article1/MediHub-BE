package mediHub_be.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mediHub_be.board.entity.Flag;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDTO {

    private Long bookmarkSeq;

    private Flag flag;

    private LocalDateTime createAt;
}
