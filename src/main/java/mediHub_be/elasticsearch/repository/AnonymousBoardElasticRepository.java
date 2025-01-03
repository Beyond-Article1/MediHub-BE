package mediHub_be.elasticsearch.repository;

import mediHub_be.elasticsearch.document.AnonymousBoardDocument;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

@ConditionalOnProperty(
        name = "spring.data.elasticsearch.repositories.enabled",
        havingValue = "true",
        matchIfMissing = false
)
public interface AnonymousBoardElasticRepository extends ElasticsearchRepository<AnonymousBoardDocument, Long>, CustomElasticRepository {

    @Query("""
            {
                       "bool": {
                         "should": [
                           {
                             "match": {
                               "anonymous_board_title.prefix": {
                                 "query": "?0",
                                 "boost": 2.0
                               }
                             }
                           },
                           {
                             "match": {
                               "anonymous_board_title": {
                                 "query": "?0",
                                 "boost": 1.0,
                                 "fuzziness": "AUTO",
                                 "prefix_length": 1
                               }
                             }
                           },
                           {
                             "wildcard": {
                               "anonymous_board_title": {
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
    // 검색 단어가 포함된 익명 게시판 전체 검색
    List<AnonymousBoardDocument> findByAnonymousBoardTitle(String findAnonymousBoardTitle);
}