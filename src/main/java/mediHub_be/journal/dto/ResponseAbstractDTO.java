package mediHub_be.journal.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResponseAbstractDTO {

    // 제목
    private String title;
    // 한글 제목
    private String koreanTitle;
    // 저널
    private String source;
    // 발행일
    private String pubDate;
    // 저널 권, 해당 페이지 범위
    private String size;
    // 저자들
    private List<String> authors;
    // DOI 값
    private String doi;
    // PMID 값
    private String pmid;
    // 논문 초록 요약본
    private String info;

    /**
     * pmid, ResponsePubmedDTO, info(초록 요약) 생성자
     * @param pmid
     * @param pubmedDTO
     * @param info
     */
    public ResponseAbstractDTO(String pmid, ResponsePubmedDTO pubmedDTO, String info) {
        this.pmid = pmid;
        this.source = pubmedDTO.getSource();
        this.title = pubmedDTO.getTitle();
        this.koreanTitle = pubmedDTO.getKoreanTitle();
        this.pubDate = pubmedDTO.getPubDate();
        this.size = pubmedDTO.getSize();
        this.authors = pubmedDTO.getAuthors();
        this.doi = pubmedDTO.getDoi();
        this.info = info;
    }
}
