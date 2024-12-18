package mediHub_be.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class OpenAiConfig {

    @Getter
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.key}")
    private String openAiKey;

    @Getter
    @Value("${openai.api.url}")
    private String apiURL;

    @Bean(name = "openAiWebClient")
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(apiURL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openAiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}
