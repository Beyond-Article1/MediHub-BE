package mediHub_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Configuration
public class BeanConfig {

    // SSE를 위한 Bean
    @Bean
    public SseEmitter sseEmitter() { return new SseEmitter(); }
}
