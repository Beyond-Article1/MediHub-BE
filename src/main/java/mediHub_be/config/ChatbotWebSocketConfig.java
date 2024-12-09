package mediHub_be.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.chat.StompHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class ChatbotWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;    // JWT 검증

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("Chatbot StompEndpoints 작동");

        registry.addEndpoint("/ws-chatbot") // 챗봇용 엔드포인트
                .setAllowedOriginPatterns("*")  // 모든 Origin 허용 -> 배포 시에는 보안을 위해 Origin을 지정 필요
                .withSockJS();                 // SockJS 사용 가능 설정


        registry.addEndpoint("/ws-chatbot")
                .setAllowedOriginPatterns("*"); // api 통신 시, withSockJS() 설정을 빼야됨
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue", "/topic");   // 메시지 구독 접두사
        registry.setApplicationDestinationPrefixes("/app");                 // 메시지 전송 접두사
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        log.info("Chatbot 메시지 인바운드 채널 설정");
        registration.interceptors(stompHandler);
    }
}
