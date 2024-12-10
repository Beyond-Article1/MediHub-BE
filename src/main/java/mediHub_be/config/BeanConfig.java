package mediHub_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {

    // SSE를 위한 Bean
    @Bean
    public SseEmitter sseEmitter() { return new SseEmitter(); }

    @Bean
    public RestTemplate restTemplate() { return new RestTemplate(); }

    // webFlux 이용
    @Bean
    public WebClient webClient(){ return WebClient.builder().build();}

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
