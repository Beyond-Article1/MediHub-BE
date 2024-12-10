package mediHub_be.openai.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Journal {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "journal_seq")
    private Long journalSeq;

    // 논문 PMID
    @Column(name = "journal_pmid", nullable = false, unique = true)
    private String journalPmid;

    // 논문 제목
    @Column(name = "journal_title", nullable = false, unique = true)
    private String journalTitle;

    // 논문 한글 제목
    @Column(name = "journal_korean_title", nullable = false)
    private String journalKorean;

    // 저자들 (List 컨버터로 변환 후 저장)
    @Convert(converter = StringListConverter.class)
    @Column(name = "journal_authors", nullable = false)
    private List<String> journalAuthors;

    // 저널 (논문 게재된 곳)
    @Column(name = "journal_journal", nullable = false)
    private String journalJournal;

    // 논문 발행일
    @Column(name = "journal_date", nullable = false)
    private String journalDate;

    // 논문 사이즈 (권, 해당 페이지)
    @Column(name = "journal_size", nullable = false)
    private String journalSize;

    // DOI 값 (링크)
    @Column(name = "journal_doi", nullable = false, unique = true)
    private String journalDoi;

    @Builder
    public Journal(String journalPmid, String journalTitle, String journalKorean,
                   List<String> journalAuthors, String journalJournal, String journalDate,
                   String journalSize, String journalDoi) {
        this.journalPmid = journalPmid;
        this.journalTitle = journalTitle;
        this.journalKorean = journalKorean;
        this.journalAuthors = journalAuthors;
        this.journalJournal = journalJournal;
        this.journalDate = journalDate;
        this.journalSize = journalSize;
        this.journalDoi = journalDoi;
    }
}
