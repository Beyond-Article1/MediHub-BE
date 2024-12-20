package mediHub_be.config.elasticsearch;

import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

// 엘라스틱서치의 인덱스를 자동으로 생성 & 관리하는 클래스
@Component
@Slf4j
@RequiredArgsConstructor
public class ElasticsearchIndexInitializer {

    // 엘라스틱서치 작업을 위한 핵심 클래스
    private final ElasticsearchOperations elasticsearchOperations;

    // 익명 게시판 검색
    @Value("${spring.elasticsearch.anonymous-board.index.name}")
    private String anonymousBoard;

    // 케이스 공유 검색
    @Value("${spring.elasticsearch.case-sharing.index.name}")
    private String caseSharing;

    // CP 검색
    @Value("${spring.elasticsearch.cp.index.name}")
    private String cp;

    // 논문 검색
    @Value("${spring.elasticsearch.journal.index.name}")
    private String journal;

    // 메디컬 라이프 검색
    @Value("${spring.elasticsearch.medical-life.index.name}")
    private String medicalLife;

    // 직원 검색
    @Value("${spring.elasticsearch.user.index.name}")
    private String user;

    @PostConstruct
    public void createIndex() {

        // 익명 게시판 인덱스 초기화
        createAnonymousBoardIndex();
        // 케이스 공유 인덱스 초기화
        createCaseSharingIndex();
        // CP 인덱스 초기화
        createCpIndex();
        // 논문 인덱스 초기화
        createJournalIndex();
        // 메디컬 라이프 인덱스 초기화
        createMedicalLifeIndex();
        // 직원 인덱스 초기화
        createUserIndex();
    }

    // 익명 게시판 인덱스 생성 메서드
    private void createAnonymousBoardIndex() {

        try {

            // 해당 인덱스 존재하지 않을 때마다 생성
            if(!elasticsearchOperations.indexOps(IndexCoordinates.of(anonymousBoard)).exists()) {

                IndexSettings.Builder settingsBuilder = new IndexSettings.Builder()
                        .numberOfShards("1")
                        .numberOfReplicas("0");
                TypeMapping.Builder mappingBuilder = new TypeMapping.Builder()
                        .properties("anonymousBoardTitle", p -> p.text(t -> t.analyzer("my-ngram-analyzer"))
                        );

                // 매핑을 JSON으로 변환
                String mappingJson = new ObjectMapper().writeValueAsString(mappingBuilder.build().properties());

                // 인덱스 생성
                elasticsearchOperations.indexOps(IndexCoordinates.of(anonymousBoard)).create();

                // 인덱스 매핑
                elasticsearchOperations.indexOps(IndexCoordinates.of(anonymousBoard))
                        .putMapping(Document.parse(mappingJson));
            }
        } catch(Exception e) {

            log.error("Anonymous Board Elasticsearch 초기화 실패", e);
        }
    }

    // 케이스 공유 인덱스 생성 메서드
    private void createCaseSharingIndex() {

        try {

            // 해당 인덱스 존재하지 않을 때마다 생성
            if(!elasticsearchOperations.indexOps(IndexCoordinates.of(caseSharing)).exists()) {

                IndexSettings.Builder settingsBuilder = new IndexSettings.Builder()
                        .numberOfShards("1")
                        .numberOfReplicas("0");
                TypeMapping.Builder mappingBuilder = new TypeMapping.Builder()
                        .properties("caseSharingTitle", p -> p.text(t -> t.analyzer("standard"))
                        );

                // 매핑을 JSON으로 변환
                String mappingJson = new ObjectMapper().writeValueAsString(mappingBuilder.build().properties());

                // 인덱스 생성
                elasticsearchOperations.indexOps(IndexCoordinates.of(caseSharing)).create();
                // 인덱스 매핑
                elasticsearchOperations.indexOps(IndexCoordinates.of(caseSharing))
                        .putMapping(Document.parse(mappingJson));
            }
        } catch(Exception e) {

            log.error("Case Sharing Elasticsearch 초기화 실패", e);
        }
    }

    // CP 인덱스 생성 메서드
    private void createCpIndex() {

        try {

            // 해당 인덱스가 존재하지 않을 때마다 생성
            if(!elasticsearchOperations.indexOps(IndexCoordinates.of(cp)).exists()) {

                IndexSettings.Builder settingsBuilder = new IndexSettings.Builder()
                        .numberOfShards("1")
                        .numberOfReplicas("0");
                TypeMapping.Builder mappingBuilder = new TypeMapping.Builder()
                        .properties("cpName", p -> p.text(t -> t.analyzer("standard"))
                );

                // 매핑을 JSON으로 변환
                String mappingJson = new ObjectMapper().writeValueAsString(mappingBuilder.build().properties());

                // 인덱스 생성
                elasticsearchOperations.indexOps(IndexCoordinates.of(cp)).create();
                // 인덱스 매핑
                elasticsearchOperations.indexOps(IndexCoordinates.of(cp))
                        .putMapping(Document.parse(mappingJson));
            }
        } catch(Exception e) {

            log.error("CP Elasticsearch 초기화 실패", e);
        }
    }

    // 논문 인덱스 생성 메서드
    private void createJournalIndex() {

        try {

            // 해당 인덱스 존재하지 않을 때마다 생성
            if(!elasticsearchOperations.indexOps(IndexCoordinates.of(journal)).exists()) {

                IndexSettings.Builder settingsBuilder = new IndexSettings.Builder()
                        .numberOfShards("1")
                        .numberOfReplicas("0");
                TypeMapping.Builder mappingBuilder = new TypeMapping.Builder()
                        .properties("journalKoreanTitle", p -> p.text(t -> t.analyzer("standard"))
                );

                // 매핑을 JSON으로 변환
                String mappingJson = new ObjectMapper().writeValueAsString(mappingBuilder.build().properties());

                // 인덱스 생성
                elasticsearchOperations.indexOps(IndexCoordinates.of(journal)).create();
                // 인덱스 매핑
                elasticsearchOperations.indexOps(IndexCoordinates.of(journal))
                        .putMapping(Document.parse(mappingJson));
            }
        } catch(Exception e) {

            log.error("Journal Elasticsearch 초기화 실패", e);
        }
    }

    // 메디컬 라이프 인덱스 생성 메서드
    private void createMedicalLifeIndex() {

        try {

            // 해당 인덱스 존재하지 않을 때마다 생성
            if(!elasticsearchOperations.indexOps(IndexCoordinates.of(medicalLife)).exists()) {

                IndexSettings.Builder settingsBuilder = new IndexSettings.Builder()
                        .numberOfShards("1")
                        .numberOfReplicas("0");
                TypeMapping.Builder mappingBuilder = new TypeMapping.Builder()
                        .properties("medicalLifeTitle", p -> p.text(t -> t.analyzer("standard"))
                        );

                // 매핑을 JSON으로 변환
                String mappingJson = new ObjectMapper().writeValueAsString(mappingBuilder.build().properties());

                // 인덱스 생성
                elasticsearchOperations.indexOps(IndexCoordinates.of(medicalLife)).create();
                // 인덱스 매핑
                elasticsearchOperations.indexOps(IndexCoordinates.of(medicalLife))
                        .putMapping(Document.parse(mappingJson));
            }
        } catch(Exception e) {

            log.error("Medical Life Elasticsearch 초기화 실패", e);
        }
    }

    // 직원 인덱스 생성 메서드
    private void createUserIndex() {

        try {

            // 해당 인덱스 존재하지 않을 때마다 생성
            if(!elasticsearchOperations.indexOps(IndexCoordinates.of(user)).exists()) {

                IndexSettings.Builder settingsBuilder = new IndexSettings.Builder()
                        .numberOfShards("1")
                        .numberOfReplicas("0");
                TypeMapping.Builder mappingBuilder = new TypeMapping.Builder()
                        .properties("userName", p -> p.text(t -> t.analyzer("standard"))
                );

                // 매핑을 JSON으로 변환
                String mappingJson = new ObjectMapper().writeValueAsString(mappingBuilder.build().properties());

                // 인덱스 생성
                elasticsearchOperations.indexOps(IndexCoordinates.of(user)).create();
                // 인덱스 매핑
                elasticsearchOperations.indexOps(IndexCoordinates.of(user))
                        .putMapping(Document.parse(mappingJson));
            }
        } catch(Exception e) {

            log.error("User Elasticsearch 초기화 실패", e);
        }
    }
}