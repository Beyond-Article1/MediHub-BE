package mediHub_be.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class ChatbotWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("Chatbot StompEndpoints 작동");

        registry.addEndpoint("/ws-chatbot") // 챗봇용 엔드포인트
                .setAllowedOriginPatterns("*")  // 모든 Origin 허용 -> 배포 시에는 보안을 위해 Origin을 지정 필요
                .withSockJS();                 // SockJS 사용 가능 설정
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/chatbot-topic"); // 챗봇 메시지 전달 브로커 경로
        registry.setApplicationDestinationPrefixes("/chatbot-app"); // 클라이언트 요청 경로 접두사
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        log.info("Chatbot 메시지 인바운드 채널 설정");
        // 필요한 경우 인증 인터셉터 추가 가능
    }
}
