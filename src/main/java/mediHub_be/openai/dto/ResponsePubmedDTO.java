package mediHub_be.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResponsePubmedDTO {
    
    // 제목
    private String title;
    // 저널
    private String source;
    // 발행일
    private String pubDate;
    // 저널 권
    private String volume;
    // 해당 페이지 범위
    private String pages;
    // 저자들
    private List<String> authors;
    // DOI 값
    private String doi;
}
