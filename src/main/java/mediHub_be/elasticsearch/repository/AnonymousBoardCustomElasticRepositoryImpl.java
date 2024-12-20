package mediHub_be.elasticsearch.repository;

import lombok.RequiredArgsConstructor;
import mediHub_be.elasticsearch.document.AnonymousBoardDocument;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AnonymousBoardCustomElasticRepositoryImpl implements AnonymousBoardCustomElasticRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<AnonymousBoardDocument> findByKeywordInField(String fieldName, String keyword) {

        Criteria criteria = new Criteria(fieldName).matches(keyword);
        CriteriaQuery query = new CriteriaQuery(criteria);

        return elasticsearchOperations
                .search(query, AnonymousBoardDocument.class)
                .map(SearchHit::getContent)
                .stream()
                .toList();
    }
}