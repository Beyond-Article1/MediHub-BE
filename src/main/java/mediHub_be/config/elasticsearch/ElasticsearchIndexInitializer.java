package mediHub_be.config.elasticsearch;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.index.Settings;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

// 엘라스틱서치 인덱스를 자동으로 생성 & 관리하는 클래스
@Component
@Slf4j
@ConditionalOnProperty(
        name = "spring.data.elasticsearch.repositories.enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class ElasticsearchIndexInitializer {

    // 엘라스틱서치 작업 위한 핵심 클래스
    private final ElasticsearchOperations elasticsearchOperations;
    private final ResourceLoader resourceLoader;

    public ElasticsearchIndexInitializer(
            ElasticsearchOperations elasticsearchOperations,
            @Qualifier("webApplicationContext") ResourceLoader resourceLoader
    ) {

        this.elasticsearchOperations = elasticsearchOperations;
        this.resourceLoader = resourceLoader;
    }

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

    @PostConstruct
    public void createIndex() {

        // 익명 게시판 인덱스 초기화
        updateAnonymousBoardIndex();
        // 케이스 공유 인덱스 초기화
        updateCaseSharingIndex();
        // CP 인덱스 초기화
        updateCpIndex();
        // 논문 인덱스 초기화
        updateJournalIndex();
        // 메디컬 라이프 인덱스 초기화
        updateMedicalLifeIndex();
    }

    // 익명 게시판 인덱스 생성 메서드
    private void updateAnonymousBoardIndex() {

        try {

            IndexOperations indexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of(anonymousBoard));
            // 엘라스틱서치 settings, mappings 적용을 위한 코드
            Resource settingsResource = resourceLoader.getResource(
                    "classpath:/elasticsearch/settings/settings.json"
            );
            String settings = StreamUtils.copyToString(settingsResource.getInputStream(), StandardCharsets.UTF_8);
            Resource mappingsResource = resourceLoader.getResource(
                    "classpath:/elasticsearch/mappings/mappings-anonymous-board.json"
            );
            String mappings = StreamUtils.copyToString(mappingsResource.getInputStream(), StandardCharsets.UTF_8);

            // 해당 인덱스가 존재하지 않을 경우 생성
            if(!indexOperations.exists()) {

                log.info("Creating new anonymous board index with settings and mappings");

                indexOperations.create(Settings.parse(settings));
                indexOperations.putMapping(Document.parse(mappings));
            } else log.info("Anonymous Board Index already exists");
        } catch(Exception e) {

            log.error("Anonymous Board Elasticsearch 초기화 실패", e);

            throw new RuntimeException("Anonymous Board Elasticsearch 인덱스 업데이트 실패", e);
        }
    }

    // 케이스 공유 인덱스 생성 메서드
    private void updateCaseSharingIndex() {

        try {

            IndexOperations indexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of(caseSharing));
            // 엘라스틱서치 settings, mappings 적용을 위한 코드
            Resource settingsResource = resourceLoader.getResource(
                    "classpath:/elasticsearch/settings/settings.json"
            );
            String settings = StreamUtils.copyToString(settingsResource.getInputStream(), StandardCharsets.UTF_8);
            Resource mappingsResource = resourceLoader.getResource(
                    "classpath:/elasticsearch/mappings/mappings-case-sharing.json"
            );
            String mappings = StreamUtils.copyToString(mappingsResource.getInputStream(), StandardCharsets.UTF_8);

            // 해당 인덱스가 존재하지 않을 경우 생성
            if(!indexOperations.exists()) {

                log.info("Creating new case sharing index with settings and mappings");

                indexOperations.create(Settings.parse(settings));
                indexOperations.putMapping(Document.parse(mappings));
            } else log.info("Case Sharing Index already exists");
        } catch(Exception e) {

            log.error("Case Sharing Elasticsearch 초기화 실패", e);

            throw new RuntimeException("Case Sharing Elasticsearch 인덱스 업데이트 실패", e);
        }
    }

    // CP 인덱스 생성 메서드
    private void updateCpIndex() {

        try {

            IndexOperations indexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of(cp));
            // 엘라스틱서치 settings, mappings 적용을 위한 코드
            Resource settingsResource = resourceLoader.getResource(
                    "classpath:/elasticsearch/settings/settings.json"
            );
            String settings = StreamUtils.copyToString(settingsResource.getInputStream(), StandardCharsets.UTF_8);
            Resource mappingsResource = resourceLoader.getResource(
                    "classpath:/elasticsearch/mappings/mappings-cp.json"
            );
            String mappings = StreamUtils.copyToString(mappingsResource.getInputStream(), StandardCharsets.UTF_8);

            // 해당 인덱스가 존재하지 않을 경우 생성
            if(!indexOperations.exists()) {

                log.info("Creating new cp index with settings and mappings");

                indexOperations.create(Settings.parse(settings));
                indexOperations.putMapping(Document.parse(mappings));
            } else log.info("CP Index already exists");
        } catch(Exception e) {

            log.error("CP Elasticsearch 초기화 실패", e);

            throw new RuntimeException("CP Elasticsearch 인덱스 업데이트 실패", e);
        }
    }

    // 논문 인덱스 생성 메서드
    private void updateJournalIndex() {

        try {

            IndexOperations indexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of(journal));
            // 엘라스틱서치 settings, mappings 적용을 위한 코드
            Resource settingsResource = resourceLoader.getResource(
                    "classpath:/elasticsearch/settings/settings.json"
            );
            String settings = StreamUtils.copyToString(settingsResource.getInputStream(), StandardCharsets.UTF_8);
            Resource mappingsResource = resourceLoader.getResource(
                    "classpath:/elasticsearch/mappings/mappings-journal.json"
            );
            String mappings = StreamUtils.copyToString(mappingsResource.getInputStream(), StandardCharsets.UTF_8);

            // 해당 인덱스가 존재하지 않을 경우 생성
            if(!indexOperations.exists()) {

                log.info("Creating new journal index with settings and mappings");

                indexOperations.create(Settings.parse(settings));
                indexOperations.putMapping(Document.parse(mappings));
            } else log.info("Journal Index already exists");
        } catch(Exception e) {

            log.error("Journal Elasticsearch 초기화 실패", e);

            throw new RuntimeException("Journal Elasticsearch 인덱스 업데이트 실패", e);
        }
    }

    // 메디컬 라이프 인덱스 생성 메서드
    private void updateMedicalLifeIndex() {

        try {

            IndexOperations indexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of(medicalLife));
            // 엘라스틱서치 settings, mappings 적용을 위한 코드
            Resource settingsResource = resourceLoader.getResource(
                    "classpath:/elasticsearch/settings/settings.json"
            );
            String settings = StreamUtils.copyToString(settingsResource.getInputStream(), StandardCharsets.UTF_8);
            Resource mappingsResource = resourceLoader.getResource(
                    "classpath:/elasticsearch/mappings/mappings-medical-life.json"
            );
            String mappings = StreamUtils.copyToString(mappingsResource.getInputStream(), StandardCharsets.UTF_8);

            // 해당 인덱스가 존재하지 않을 경우 생성
            if(!indexOperations.exists()) {

                log.info("Creating new medical life index with settings and mappings");

                indexOperations.create(Settings.parse(settings));
                indexOperations.putMapping(Document.parse(mappings));
            } else log.info("Medical Life Index already exists");
        } catch(Exception e) {

            log.error("Medical Life Elasticsearch 초기화 실패", e);

            throw new RuntimeException("Medical Life Elasticsearch 인덱스 업데이트 실패", e);
        }
    }
}