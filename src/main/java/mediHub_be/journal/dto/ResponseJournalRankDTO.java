package mediHub_be.journal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mediHub_be.journal.entity.Journal;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseJournalRankDTO {

    // 논문 시퀀스
    private Long journalSeq;
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
    // 조회수
    private Long count;
    // 북마크 현황
    private boolean isBookmark = false;

    // === 조회수 생성 === //
    public ResponseJournalRankDTO(Journal journal, Long count){
        this.journalSeq = journal.getJournalSeq();
        this.title = journal.getJournalTitle();
        this.koreanTtile = journal.getJournalKoreanTitle();
        this.source = journal.getJournalJournal();
        this.pubDate = journal.getJournalDate();
        this.size = journal.getJournalSize();
        this.authors = journal.getJournalAuthors();
        this.doi = journal.getJournalDoi();
        this.pmid = journal.getJournalPmid();

        this.count = Objects.requireNonNullElse(count, 0L);
    }

    // === 북마크 유무 넣기 === //
    public void isBookmark(boolean isBookmark){
        this.isBookmark = isBookmark;
    }
}
