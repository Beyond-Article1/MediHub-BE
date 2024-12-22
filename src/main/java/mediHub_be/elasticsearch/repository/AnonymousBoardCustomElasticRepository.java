package mediHub_be.elasticsearch.repository;

import mediHub_be.elasticsearch.document.AnonymousBoardDocument;

import java.util.List;

public interface AnonymousBoardCustomElasticRepository {

    List<AnonymousBoardDocument> findByKeywordInField(String fieldName, String keyword);
}