package mediHub_be.config.elasticsearch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "mediHub_be.elasticsearch.repository")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris}")
    private String elasticsearchUris;

    @Value("${spring.elasticsearch.username}")
    private String elasticsearchUsername;

    @Value("${spring.elasticsearch.password}")
    private String elasticsearchPassword;

    @Value("${spring.elasticsearch.anonymous-board.index.name}")
    private String anonymousBoard;

    @Value("${spring.elasticsearch.case-sharing.index.name}")
    private String caseSharing;

    @Value("${spring.elasticsearch.cp.index.name}")
    private String cp;

    @Value("${spring.elasticsearch.journal.index.name}")
    private String journal;

    @Value("${spring.elasticsearch.medical-life.index.name}")
    private String medicalLife;

    @Override
    public ClientConfiguration clientConfiguration() {

        return ClientConfiguration.builder()
                .connectedTo(elasticsearchUris.replace("http://",""))
                // 기본 인증
                .withBasicAuth(elasticsearchUsername, elasticsearchPassword)
                // 소켓 타임아웃 설정 (30초)
                .withSocketTimeout(30000)
                // 연결 타임아웃 설정 (30초)
                .withConnectTimeout(30000)
                .build();
    }
}