package mediHub_be.elasticsearch.repository;

import mediHub_be.elasticsearch.document.UserDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UserFindRepository extends ElasticsearchRepository<UserDocument, String> {

    // 검색 단어가 포함된 직원 전체 검색
    @Query("{\"wildcard\": {\"user_name\": \"*?0*\"}}")
    List<UserDocument> findByUserName(String findUserName);
}