package mediHub_be.anonymousBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnonymousBoardUpdateRequestDTO {

    private String AnonymousBoardTitle;
    private String AnonymousBoardContent;
    private List<String> keywords;
}