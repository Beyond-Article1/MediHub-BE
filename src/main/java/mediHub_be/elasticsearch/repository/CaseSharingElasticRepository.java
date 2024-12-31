package mediHub_be.elasticsearch.repository;

import mediHub_be.elasticsearch.document.CaseSharingDocument;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

@ConditionalOnProperty(
        name = "spring.data.elasticsearch.repositories.enabled",
        havingValue = "true",
        matchIfMissing = false
)
public interface CaseSharingElasticRepository extends ElasticsearchRepository<CaseSharingDocument, Long>, CustomElasticRepository {

    @Query("""
            {
                       "bool": {
                         "should": [
                           {
                             "match": {
                               "case_sharing_title.prefix": {
                                 "query": "?0",
                                 "boost": 2.0
                               }
                             }
                           },
                           {
                             "match": {
                               "case_sharing_title": {
                                 "query": "?0",
                                 "boost": 1.0,
                                 "fuzziness": "AUTO",
                                 "prefix_length": 1
                               }
                             }
                           },
                           {
                             "wildcard": {
                               "case_sharing_title": {
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
    // 검색 단어가 포함된 케이스 공유 전체 검색
    List<CaseSharingDocument> findByCaseSharingTitle(String findCaseSharingTitle);
}