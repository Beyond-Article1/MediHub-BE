package mediHub_be.elasticsearch.repository;

import mediHub_be.elasticsearch.document.MedicalLifeDocument;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

@ConditionalOnProperty(
        name = "spring.data.elasticsearch.repositories.enabled",
        havingValue = "true",
        matchIfMissing = false
)
public interface MedicalLifeElasticRepository extends ElasticsearchRepository<MedicalLifeDocument, Long>, CustomElasticRepository {

    @Query("""
            {
                       "bool": {
                         "should": [
                           {
                             "match": {
                               "medical_life_title.prefix": {
                                 "query": "?0",
                                 "boost": 2.0
                               }
                             }
                           },
                           {
                             "match": {
                               "medical_life_title": {
                                 "query": "?0",
                                 "boost": 1.0,
                                 "fuzziness": "AUTO",
                                 "prefix_length": 1
                               }
                             }
                           },
                           {
                             "wildcard": {
                               "medical_life_title": {
                                 "value": "*?0*",
                                 "boost": 0.5
                               }
                             }
                           }
                         ],
                         "minimum_should_match": 1
                       }
                     }
            """)
    // 검색 단어가 포함된 메디컬 라이프 전체 검색
    List<MedicalLifeDocument> findByMedicalLifeTitle(String findMedicalLifeTitle);
}