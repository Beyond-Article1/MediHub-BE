package mediHub_be.notify.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.notify.dto.NotifyDTO;
import mediHub_be.notify.entity.NotiReadStatus;
import mediHub_be.notify.entity.NotiType;
import mediHub_be.notify.entity.Notify;
import mediHub_be.notify.repository.NotifyRepository;
import mediHub_be.notify.repository.SseRepository;
import mediHub_be.notify.repository.SseRepositoryImpl;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyService {
    
    // 연결 지속시간
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final SseRepositoryImpl sseRepository;
    private final NotifyRepository notifyRepository;
    private final UserRepository userRepository;

    private final static Long userSeq = 1L;
    private final static String userEmail = "email";

    public SseEmitter subscribe(String lastEventId) {

//        Long userSeq = SecurityUtil.getCurrentUserSeq();



//        User user = userRepository.findByUserSeq(userSeq);

//        String userId = user.getUserId;


        String emitterId = makeTimeIncludeId(userEmail);
        SseEmitter emitter = sseRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> sseRepository.deleteById(emitterId));
        emitter.onTimeout(() -> sseRepository.deleteById(emitterId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(userEmail);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userEmail=" + userEmail + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userEmail, emitterId, emitter);
        }

        return emitter;
    }

    private String makeTimeIncludeId(String email) {
        return email + "_" + System.currentTimeMillis();
    }

    // 알림 보내기
    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data)
            );
        } catch (IOException exception) {
            sseRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    public void sendLostData(String lastEventId, String userEmail, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = sseRepository.findAllEventCacheStartWithByMemberId(String.valueOf(userEmail));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    // send()
//    public void send(User receiver, NotiType notiType, String content, String url) {
    public void send(NotiType notiType, String content, String url) {
        User receiver = userRepository.findByUserId("dlacofbs").orElseThrow(() -> new RuntimeException("User not found"));

        log.debug("receiver 확인 {}", receiver);

        Notify notification = notifyRepository.save(createNotification(receiver, notiType, content, url));

        String receiverEmail = receiver.getUserEmail();
        String eventId = receiverEmail + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = sseRepository.findAllEmitterStartWithByMemberId(receiverEmail);
        emitters.forEach(
                (key, emitter) -> {
                    sseRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotifyDTO.Response.createResponse(notification));
                }
        );
    }

    private Notify createNotification(User receiver, NotiType notificationType, String content, String url) {
        return Notify.builder()
                .receiver(receiver)
                .notiType(notificationType)
                .notiContent(content)
                .notiUrl(url)
                .isRead(NotiReadStatus.N)
                .build();
    }

}
