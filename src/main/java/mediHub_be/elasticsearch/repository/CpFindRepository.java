package mediHub_be.elasticsearch.repository;

import mediHub_be.elasticsearch.document.CpDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CpFindRepository extends ElasticsearchRepository<CpDocument, String> {

    // 검색 단어가 포함된 CP 전체 검색
    @Query("{\"wildcard\": {\"cp_name\": \"*?0*\"}}")
    List<CpDocument> findByCpName(String findCpName);
}