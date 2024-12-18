package mediHub_be.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.security.util.JwtUtil;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("Stomp Handler 작동");

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        // STOMP Command 확인
        StompCommand command = headerAccessor.getCommand();
        log.info("STOMP Command: {}", command);

        // CONNECT 또는 SEND Frame에 대해서만 토큰 검증 수행
        if (StompCommand.CONNECT.equals(command) || StompCommand.SEND.equals(command)) {
            log.info("{} 요청 시 headerAccessor 확인: {}", command, headerAccessor);

            // Authorization 헤더 값 확인
            String authorizationHeader = headerAccessor.getFirstNativeHeader("Authorization");

            // Authorization 헤더가 없거나 값이 비어있으면 예외 발생
            if (authorizationHeader == null || authorizationHeader.trim().isEmpty()) {
                throw new MessageDeliveryException("Authorization header is missing or empty");
            }

            // Bearer 접두어 체크 후 토큰 추출
            String token = extractToken(authorizationHeader);
            if (token == null) {
                throw new MessageDeliveryException("Authorization header is malformed, missing 'Bearer ' prefix");
            }

            // 토큰 검증
            if (!jwtUtil.validateToken(token)) {
                throw new MessageDeliveryException("Invalid or expired JWT token");
            }
        }

        return message; // CONNECT 및 SEND 외의 프레임은 토큰 검증 없이 통과
    }

    // Authorization 헤더에서 Bearer 토큰을 추출하는 메소드
    private String extractToken(String authorizationHeader) {
        if (authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(BEARER_PREFIX.length()); // "Bearer " 뒤의 실제 토큰 부분만 반환
        }
        return null;
    }

    @EventListener
    public void handleWebSocketConnectionListener(SessionConnectedEvent event){
        log.info("사용자 입장");
    }

    @EventListener
    public void handleWebSocketDisconnectionListener(SessionDisconnectEvent event) {
        log.info("사용자 퇴장");
    }

}
