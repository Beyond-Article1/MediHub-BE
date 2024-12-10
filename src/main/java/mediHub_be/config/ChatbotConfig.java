package mediHub_be.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Getter
@Configuration
public class ChatbotConfig {

    @Value("${openai.api.key.bot}")
    private String chatbotApiKey;

    @Value("${openai.api.url}")
    private String apiUrl;
}
