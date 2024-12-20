package mediHub_be.elasticsearch.repository;

import mediHub_be.elasticsearch.document.AnonymousBoardDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AnonymousBoardElasticRepository extends ElasticsearchRepository<AnonymousBoardDocument, Long>, AnonymousBoardCustomElasticRepository {
}