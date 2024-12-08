package mediHub_be.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ChatbotConfig {

    @Value("${openai.api.key.bot}")
    private String chatbotApiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getChatbotApiKey() {
        return chatbotApiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }
}
