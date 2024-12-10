package mediHub_be.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

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

    public OpenAiConfig(@Value("${openai.model}") String model,@Value("${openai.api.key}") String openAiKey,@Value("${openai.api.url}") String apiURL) {
        this.model = model;
        this.openAiKey = openAiKey;
        this.apiURL = apiURL;
    }

    @Bean(name = "openAiWebClient")
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl(apiURL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openAiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}
