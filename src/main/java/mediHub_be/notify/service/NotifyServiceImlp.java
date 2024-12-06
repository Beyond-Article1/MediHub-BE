package mediHub_be.notify.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.entity.Flag;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.notify.dto.NotifyDTO;
import mediHub_be.notify.entity.NotiReadStatus;
import mediHub_be.notify.entity.NotiType;
import mediHub_be.notify.entity.Notify;
import mediHub_be.notify.repository.NotifyRepository;
import mediHub_be.notify.repository.SseRepositoryImpl;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyServiceImlp implements NotifyService{
    
    // 연결 지속시간
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final SseRepositoryImpl sseRepository;
    private final NotifyRepository notifyRepository;

    public SseEmitter subscribe(String userId, String lastEventId) {

        String emitterId = makeTimeIncludeId(userId);
        SseEmitter emitter = sseRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> sseRepository.deleteById(emitterId));
        emitter.onTimeout(() -> sseRepository.deleteById(emitterId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(userId);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + userId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userId, emitterId, emitter);
        }

        return emitter;
    }

    private String makeTimeIncludeId(String userId) {
        return userId + "_" + System.currentTimeMillis();
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

    public void sendLostData(String lastEventId, String userId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = sseRepository.findAllEventCacheStartWithByMemberId(String.valueOf(userId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    // 받는 사람이 한명인 경우 (ex. 게시글에 대한 댓글 발생 -> 게시글 작성자에게만 알림 생성)
    @Override
    public void send(User sender, User receiver, NotiType notiType, String url) {

        log.debug("receiver 확인 {}", receiver);

        Notify notification = notifyRepository.save(createNotification(receiver, notiType, notiType.getMessage(), url));

        String receiverUserId = receiver.getUserId();
        String eventId = receiverUserId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = sseRepository.findAllEmitterStartWithByMemberId(receiverUserId);
        emitters.forEach(
                (key, emitter) -> {
                    sseRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotifyDTO.Response.createResponse(notification));
                }
        );
    }

    // 받는 사람이 여러명인 경우 (ex. 게시글 또는 케이스공유 작성시 팔로워들한테 알림 발생)
    @Override
    public void send(User sender, List<User> receivers, NotiType notiType, String url) {

        log.debug("sender 확인 {}", sender);

        for (User receiver : receivers) {
            Notify notification = notifyRepository.save(createNotification(receiver, notiType, notiType.getMessage(), url));

            String receiverUserId = receiver.getUserId();
            String eventId = receiverUserId + "_" + System.currentTimeMillis();
            Map<String, SseEmitter> emitters = sseRepository.findAllEmitterStartWithByMemberId(receiverUserId);
            emitters.forEach(
                    (key, emitter) -> {
                        sseRepository.saveEventCache(key, notification);
                        sendNotification(emitter, eventId, key, NotifyDTO.Response.createResponse(notification));
                    }
            );
        }

    }

    private Notify createNotification(User receiver, NotiType notificationType, String content, String url) {
//    private Notify createNotification(User receiver, Flag flag, NotiType notificationType, String content, String url) {
        return Notify.builder()
                .receiver(receiver)
//                .flag(flag)
                .notiType(notificationType)
                .notiContent(content)
                .notiUrl(url)
                .isRead(NotiReadStatus.N)
                .build();
    }

}
