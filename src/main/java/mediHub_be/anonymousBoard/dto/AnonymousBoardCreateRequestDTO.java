package mediHub_be.anonymousBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnonymousBoardCreateRequestDTO {

    private String anonymousBoardTitle;
    private String anonymousBoardContent;
    private List<MultipartFile> imageList;
    private List<String> keywords;
}