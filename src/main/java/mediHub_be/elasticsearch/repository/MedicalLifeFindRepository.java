package mediHub_be.elasticsearch.repository;

import mediHub_be.elasticsearch.document.MedicalLifeDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface MedicalLifeFindRepository extends ElasticsearchRepository<MedicalLifeDocument, String> {

    // 검색 단어가 포함된 메디컬 라이프 전체 검색
    @Query("{\"wildcard\": {\"medical_life_title\": \"*?0*\"}}")
    List<MedicalLifeDocument> findByMedicalLifeTitle(String findMedicalLifeTitle);
}