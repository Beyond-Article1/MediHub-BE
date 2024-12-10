package mediHub_be.journal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import mediHub_be.journal.entity.Journal;

import java.util.List;

@Data
@AllArgsConstructor
public class ResponsePubmedDTO {

    // 제목
    private String title;
    // 한글 제목
    private String koreanTtile;
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
    // toEntity
    public Journal toEntity() {
        return Journal.builder()
                .journalPmid(this.getPmid())
                .journalTitle(this.getTitle())
                .journalKorean(this.getKoreanTtile())
                .journalAuthors(this.getAuthors())
                .journalJournal(this.getSource())
                .journalDate(this.getPubDate())
                .journalSize(this.getSize())
                .journalDoi(this.getDoi())
                .build();
    }

    //
}
