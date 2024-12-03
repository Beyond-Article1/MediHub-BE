package mediHub_be.notify.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.notify.dto.NotifyDTO;
import mediHub_be.notify.entity.Notify;
import mediHub_be.notify.entity.User;
import mediHub_be.notify.repository.NotifyRepository;
import mediHub_be.notify.repository.SseRepository;
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

    private final SseRepository sseRepository;
    private final NotifyRepository notifyRepository;

    private final static Long userSeq = 1L;
    private final static String userEmail = "user1";

    public SseEmitter subscribe(String lastEventId) {

//        Long userSeq = SecurityUtil.getCurrentUserSeq();



//        User user = userRepository.findByUserSeq(userSeq);

//        String userId = user.getUserId;


        String emitterId = makeTimeIncludeId(userEmail); // (1-2)
        SseEmitter emitter = sseRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT)); // (1-3)
        // (1-4)
        emitter.onCompletion(() -> sseRepository.deleteById(emitterId));
        emitter.onTimeout(() -> sseRepository.deleteById(emitterId));

        // (1-5) 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(userEmail);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userEmail=" + userEmail + "]");

        // (1-6) 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userEmail, emitterId, emitter);
        }

        return emitter; // (1-7)
    }

    private String makeTimeIncludeId(String email) {
        return email + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) { // (4)
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

    private boolean hasLostData(String lastEventId) { // (5)
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, String userEmail, String emitterId, SseEmitter emitter) { // (6)
        Map<String, Object> eventCaches = sseRepository.findAllEventCacheStartWithByMemberId(String.valueOf(userEmail));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    // [2] send()
    //@Override
    public void send(User receiver, Notify.NotiType notiType, String content, String url) {
        Notify notification = notifyRepository.save(createNotification(receiver, notiType, content, url)); // (2-1)

        String receiverEmail = receiver.getUserEmail(); // (2-2)
        String eventId = receiverEmail + "_" + System.currentTimeMillis(); // (2-3)
        Map<String, SseEmitter> emitters = sseRepository.findAllEmitterStartWithByMemberId(receiverEmail); // (2-4)
        emitters.forEach( // (2-5)
                (key, emitter) -> {
                    sseRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotifyDTO.Response.createResponse(notification));
                }
        );
    }

    private Notify createNotification(User receiver, Notify.NotiType notificationType, String content, String url) { // (7)
        return Notify.builder()
                .receiver(receiver)
                .notiType(notificationType)
                .notiContent(content)
                .notiUrl(url)
                .isRead(Notify.NotiReadStatus.N)
                .build();
    }
}
