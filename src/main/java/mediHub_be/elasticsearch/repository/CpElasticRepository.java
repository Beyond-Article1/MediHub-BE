package mediHub_be.elasticsearch.repository;

import mediHub_be.elasticsearch.document.CpDocument;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

@ConditionalOnProperty(
        name = "spring.data.elasticsearch.repositories.enabled",
        havingValue = "true",
        matchIfMissing = false
)
public interface CpElasticRepository extends ElasticsearchRepository<CpDocument, Long>, CustomElasticRepository {

    @Query("""
            {
                       "bool": {
                         "should": [
                           {
                             "match": {
                               "cp_name.prefix": {
                                 "query": "?0",
                                 "boost": 2.0
                               }
                             }
                           },
                           {
                             "match": {
                               "cp_name": {
                                 "query": "?0",
                                 "boost": 1.0,
                                 "fuzziness": "AUTO",
                                 "prefix_length": 1
                               }
                             }
                           },
                           {
                             "wildcard": {
                               "cp_name": {
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
    // 검색 단어가 포함된 CP 전체 검색
    List<CpDocument> findByCpName(String findCpName);
}