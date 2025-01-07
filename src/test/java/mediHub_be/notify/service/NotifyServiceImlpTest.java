package mediHub_be.notify.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import mediHub_be.board.entity.Flag;
import mediHub_be.notify.entity.NotiType;
import mediHub_be.notify.entity.Notify;
import mediHub_be.notify.repository.NotifyRepository;
import mediHub_be.notify.repository.SseRepositoryImpl;
import mediHub_be.part.entity.Part;
import mediHub_be.ranking.entity.Ranking;
import mediHub_be.user.entity.User;
import mediHub_be.user.entity.UserAuth;
import mediHub_be.user.entity.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotifyServiceImlpTest {

    @Mock
    NotifyRepository notifyRepository;

    @Mock
    SseRepositoryImpl sseRepository;

    @InjectMocks
    NotifyServiceImlp notifyServiceImlp;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    @Test
    @DisplayName("SSE 연결 타임아웃 콜백 테스트")
    void subscribeTimeOutTest() {
        // given
        String userId = "testUser";
        SseEmitter mockEmitter = spy(new SseEmitter(DEFAULT_TIMEOUT));

        when(sseRepository.save(anyString(), any(SseEmitter.class))).thenReturn(mockEmitter);

        // when
        notifyServiceImlp.subscribe(userId, userId);

        // 캡처: onTimeout에 등록된 콜백 실행
        ArgumentCaptor<Runnable> onTimeoutCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(mockEmitter).onTimeout(onTimeoutCaptor.capture());

        // 타임아웃 콜백 실행
        onTimeoutCaptor.getValue().run();

        // then
        verify(sseRepository, times(1)).deleteById(anyString());
    }

    @Test
    @DisplayName("SSE 연결 완료 콜백 테스트")
    void subscribe() {
        // given
        String userId = "testUser";
        SseEmitter mockEmitter = spy(new SseEmitter(DEFAULT_TIMEOUT));

        when(sseRepository.save(anyString(), any(SseEmitter.class))).thenReturn(mockEmitter);

        // when
        notifyServiceImlp.subscribe(userId, userId);

        // 캡처: onCompletion에 등록된 콜백 실행
        ArgumentCaptor<Runnable> onCompletionCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(mockEmitter).onCompletion(onCompletionCaptor.capture());

        // 완료 콜백 실행
        onCompletionCaptor.getValue().run();

        // then
        verify(sseRepository, times(1)).deleteById(anyString());
    }


    @Test
    @DisplayName("유실된 이벤트 데이터 전송 테스트")
    void sendLostData(){
    }

    @Test
    @DisplayName("전송 테스트")
    void send() {
    }


    @Test
    void testSend() {
    }

    @Test
    void sendChat() {
    }

    @Test
    void notiAll() {
    }

    @Test
    void readNotify() {
    }

    @Test
    void deleteNotify() {
    }

    @Test
    void readAllNotify() {
    }

    @Test
    void deleteAllNotify() {
    }
}