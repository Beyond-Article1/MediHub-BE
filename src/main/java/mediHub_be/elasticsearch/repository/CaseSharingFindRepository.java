package mediHub_be.elasticsearch.repository;

import mediHub_be.elasticsearch.document.CaseSharingDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CaseSharingFindRepository extends ElasticsearchRepository<CaseSharingDocument, String> {

    // 검색 단어가 포함된 케이스 공유 전체 검색
    @Query("{\"wildcard\": {\"case_sharing_title\": \"*?0*\"}}")
    List<CaseSharingDocument> findByCaseSharingTitle(String findCaseSharingTitle);
}