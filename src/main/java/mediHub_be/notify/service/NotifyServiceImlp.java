package mediHub_be.notify.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private final ObjectMapper objectMapper = new ObjectMapper();

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

    // 아이디 생셩
    private String makeTimeIncludeId(String userId) {
        return userId + "_" + System.currentTimeMillis();
    }

    // 알림 보내기
    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            // 데이터를 JSON 문자열로 직렬화
            String jsonData = objectMapper.writeValueAsString(data);

            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(jsonData)
            );
        } catch (IOException exception) {
            sseRepository.deleteById(emitterId);
        }
    }

    // 미수신 알림이 있는 경우
    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    // 마지막 데이터들 넘겨보내기
    public void sendLostData(String lastEventId, String userId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = sseRepository.findAllEventCacheStartWithByMemberId(String.valueOf(userId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    // 받는 사람이 한명인 경우 (ex. 게시글에 대한 댓글 발생 -> 게시글 작성자에게만 알림 생성)
    @Override
    @Transactional
    public void send(User sender, User receiver, Flag flag, NotiType notiType, String url) {

        log.debug("receiver 확인 {}", receiver);

        Notify notification = notifyRepository.save(createNotification(receiver, flag, notiType, notiType.getMessage(), url, sender.getUserId(), sender.getPart().getPartName()));

        String receiverUserId = receiver.getUserId();
        String eventId = receiverUserId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = sseRepository.findAllEmitterStartWithByMemberId(receiverUserId);
        emitters.forEach(
                (key, emitter) -> {
                    sseRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotifyDTO.createResponse(notification, sender.getUserName(), sender.getPart().getPartName()));
                }
        );
    }

    // 받는 사람이 여러명인 경우 (ex. 게시글 또는 케이스공유 작성시 팔로워들한테 알림 발생)
    @Override
    @Transactional
    public void send(User sender, List<User> receivers, Flag flag, NotiType notiType, String url) {

        log.debug("sender 확인 {}", sender);

        for (User receiver : receivers) {
            Notify notification = notifyRepository.save(createNotification(receiver, flag, notiType, notiType.getMessage(), url, sender.getUserId(), sender.getPart().getPartName()));

            String receiverUserId = receiver.getUserId();
            String eventId = receiverUserId + "_" + System.currentTimeMillis();
            Map<String, SseEmitter> emitters = sseRepository.findAllEmitterStartWithByMemberId(receiverUserId);
            emitters.forEach(
                    (key, emitter) -> {
                        sseRepository.saveEventCache(key, notification);
                        sendNotification(emitter, eventId, key, NotifyDTO.createResponse(notification, sender.getUserName(), sender.getPart().getPartName()));
                    }
            );
        }

    }

    // 알림 생성 로직
//    private Notify createNotification(User receiver, NotiType notificationType, String content, String url, String senderUserName, String senderUserPart) {
    private Notify createNotification(User receiver, Flag flag, NotiType notificationType, String content, String url, String senderUserName, String senderUserPart) {
        return Notify.builder()
                .receiver(receiver)
                .flag(flag)
                .notiType(notificationType)
                .notiContent(content)
                .notiUrl(url)
                .isRead(NotiReadStatus.N)
                .senderUserName(senderUserName)
                .senderUserPart(senderUserPart)
                .build();
    }

    // 알림 전체 조회
    @Transactional
    public List<NotifyDTO> notiAll(String userId){

        return notifyRepository.findAllByUserId(userId)
                .stream()
                .map(NotifyDTO::new)
                .toList();
    }

    // 알림 단일 읽음
    @Transactional
    public void readNotify(String userId, Long notiSeq){

        Notify notify = checkNotifyAndUserId(userId, notiSeq);

        // 읽음 처리
        notify.notiRead();
    }

    // 알림 단일 삭제
    @Transactional
    public void deleteNotify(String userId, Long notiSeq){

        Notify notify = checkNotifyAndUserId(userId, notiSeq);

        // 삭제 처리
        notifyRepository.delete(notify);
    }

    // 알림 전체 읽음
    @Transactional
    public void readAllNotify(String userId){

        List<Notify> notReadNotifyByUserId =
                notifyRepository.findNotReadNotifyByUserId(userId);

        notReadNotifyByUserId
                .forEach(Notify::notiRead);
    }

    // 알림 전체 삭제
    @Transactional
    public void deleteAllNotify(String userId){

        List<Notify> allByUserId = notifyRepository.findAllByUserId(userId);

        notifyRepository.deleteAll(allByUserId);
    }

    // Notify의 수신자 아이디와 로그인 한 요청자의 ID 비교
    private Notify checkNotifyAndUserId(String userId, Long notiSeq){
        Notify notify = notifyRepository.findById(notiSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_NOTIFY));

        if (userId.equals(notify.getReceiver().getUserId())){
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        return notify;
    }
}
