package mediHub_be.elasticsearch.document;

import lombok.*;
import mediHub_be.config.elasticsearch.BaseSearchDocument;
import mediHub_be.journal.entity.Journal;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 논문 인덱스 (DB table 개념)에 데이터 저장
@Document(indexName = "journal")
public class JournalDocument extends BaseSearchDocument {

    @Field(name = "journal_korean_title", type = FieldType.Text, analyzer = "korean")
    private String journalKoreanTitle;

    public static JournalDocument from(Journal journal) {

        JournalDocument journalDocument = JournalDocument.builder()
                .journalKoreanTitle(journal.getJournalKoreanTitle())
                .build();

        journalDocument.setId(journal.getJournalSeq());

        return journalDocument;
    }
}