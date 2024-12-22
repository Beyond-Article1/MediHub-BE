package mediHub_be.journal.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import mediHub_be.journal.entity.Journal;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class ResponsePubmedDTO {

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

    // === 변환 로직 === //

    // JSON 파싱 시 authors 필드를 쉼표로 구분하여 리스트로 변환
    @JsonCreator
    public ResponsePubmedDTO(
            @JsonProperty("title") String title,
            @JsonProperty("koreanTitle") String koreanTitle,
            @JsonProperty("source") String source,
            @JsonProperty("pubDate") String pubDate,
            @JsonProperty("size") String size,
            @JsonProperty("authors") String authors, // authors는 이제 String으로 받음
            @JsonProperty("doi") String doi,
            @JsonProperty("pmid") String pmid) {
        this.title = title;
        this.koreanTitle = koreanTitle;
        this.source = source;
        this.pubDate = pubDate;
        this.size = size;
        this.authors = authors != null ? Arrays.asList(authors.split(", ")) : null; // 쉼표로 분할하여 리스트로 변환
        this.doi = doi;
        this.pmid = pmid;
    }

    // toEntity
    public Journal toEntity() {
        return Journal.builder()
                .journalPmid(this.getPmid())
                .journalTitle(this.getTitle())
                .journalKorean(this.getKoreanTitle())
                .journalAuthors(this.getAuthors())
                .journalJournal(this.getSource())
                .journalDate(this.getPubDate())
                .journalSize(this.getSize())
                .journalDoi(this.getDoi())
                .build();
    }

    //
}
