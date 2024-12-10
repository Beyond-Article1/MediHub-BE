package mediHub_be.journal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mediHub_be.journal.entity.Journal;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseJournalLogDTO {
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
    // 조회한 시간 (북마크한 시간)
    private LocalDateTime createdAt;

    // === Journal기반 DTO 생성 === //
    public ResponseJournalLogDTO(Journal journal, LocalDateTime createdAt) {
        this.journalSeq = journal.getJournalSeq();
        this.title = journal.getJournalTitle();
        this.koreanTtile = journal.getJournalKorean();
        this.source = journal.getJournalJournal();
        this.pubDate = journal.getJournalDate();
        this.size = journal.getJournalSize();
        this.authors = journal.getJournalAuthors();
        this.doi = journal.getJournalDoi();
        this.pmid = journal.getJournalPmid();
        this.createdAt = createdAt;
    }
}
