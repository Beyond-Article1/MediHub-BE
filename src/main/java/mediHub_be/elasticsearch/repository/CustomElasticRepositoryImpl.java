package mediHub_be.elasticsearch.repository;

import lombok.RequiredArgsConstructor;
import mediHub_be.elasticsearch.document.*;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomElasticRepositoryImpl implements CustomElasticRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<AnonymousBoardDocument> findAnonymousBoardDocumentByKeywordInField(
            String fieldName,
            String keyword
    ) {

        Criteria criteria = new Criteria(fieldName).matches(keyword);
        CriteriaQuery query = new CriteriaQuery(criteria);

        return elasticsearchOperations
                .search(query, AnonymousBoardDocument.class)
                .map(SearchHit::getContent)
                .stream()
                .toList();
    }

    @Override
    public List<CaseSharingDocument> findCaseSharingDocumentByKeywordInField(String fieldName, String keyword) {

        Criteria criteria = new Criteria(fieldName).matches(keyword);
        CriteriaQuery query = new CriteriaQuery(criteria);

        return elasticsearchOperations
                .search(query, CaseSharingDocument.class)
                .map(SearchHit::getContent)
                .stream()
                .toList();
    }

    @Override
    public List<CpDocument> findCpDocumentByKeywordInField(String fieldName, String keyword) {

        Criteria criteria = new Criteria(fieldName).matches(keyword);
        CriteriaQuery query = new CriteriaQuery(criteria);

        return elasticsearchOperations
                .search(query, CpDocument.class)
                .map(SearchHit::getContent)
                .stream()
                .toList();
    }

    @Override
    public List<JournalDocument> findJournalDocumentByKeywordInField(String fieldName, String keyword) {

        Criteria criteria = new Criteria(fieldName).matches(keyword);
        CriteriaQuery query = new CriteriaQuery(criteria);

        return elasticsearchOperations
                .search(query, JournalDocument.class)
                .map(SearchHit::getContent)
                .stream()
                .toList();
    }

    @Override
    public List<MedicalLifeDocument> findMedicalLifeDocumentByKeywordInField(String fieldName, String keyword) {

        Criteria criteria = new Criteria(fieldName).matches(keyword);
        CriteriaQuery query = new CriteriaQuery(criteria);

        return elasticsearchOperations
                .search(query, MedicalLifeDocument.class)
                .map(SearchHit::getContent)
                .stream()
                .toList();
    }
}