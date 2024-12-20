package mediHub_be.elasticsearch.repository;

import mediHub_be.elasticsearch.document.JournalDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface JournalFindRepository extends ElasticsearchRepository<JournalDocument, String> {

    // 검색 단어가 포함된 논문 전체 검색
    @Query("{\"wildcard\": {\"journal_korean_title\": \"*?0*\"}}")
    List<JournalDocument> findByJournalKoreanTitle(String findJournalKoreanTitle);
}