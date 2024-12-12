package mediHub_be.chatbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.chatbot.entity.ChatbotMessage;
import mediHub_be.chatbot.entity.ChatbotSession;
import mediHub_be.chatbot.repository.ChatbotMessageRepository;
import mediHub_be.chatbot.repository.ChatbotSessionRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.common.utils.DateTimeUtil;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import mediHub_be.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotRestService {
    private final ChatbotSessionRepository chatbotSessionRepository;
    private final ChatbotMessageRepository chatbotMessageRepository;
    private final UserService userService;

    // 1. 새로운 채팅 세션 생성
    @Transactional
    public String createNewChatSession(String userId, String title) {
        User user = userService.findByUserId(userId);

        ChatbotSession session = ChatbotSession.builder()
                .userSeq(user.getUserSeq())
                .title(title)
                .createdAt(DateTimeUtil.localDateTimeToLocalDateTime(LocalDateTime.now()))
                .build();

        ChatbotSession savedSession = chatbotSessionRepository.save(session);
        log.info("새로운 채팅 세션 생성: {}", savedSession.getId());
        return savedSession.getId();
    }

    // 2, 특정 채팅 세션의 채팅 메세지 저장
    @Transactional
    public void saveMessage(String userId, String sessionId, String sender, String content) {
        userService.findByUserId(userId);

        ChatbotMessage message = ChatbotMessage.builder()
                .sessionId(sessionId)
                .sender(sender)
                .content(content)
                .timestamp(DateTimeUtil.localDateTimeToLocalDateTime(LocalDateTime.now()))
                .build();

        chatbotMessageRepository.save(message);
        log.info("메시지 저장 - 세션: {}, 발신자: {}, 내용: {}", sessionId, sender, content);

        // 세션 업데이트
        ChatbotSession session = chatbotSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 세션입니다."));

        session.updateLastMessage(DateTimeUtil.localDateTimeToLocalDateTime(LocalDateTime.now()), content);
        chatbotSessionRepository.save(session);
        log.info("세션 업데이트 - ID: {}, 마지막 메시지: {}", sessionId, content);
    }

    // 3. 특정 채팅 세션의 기록 불러오기
    @Transactional(readOnly = true)
    public List<ChatbotMessage> getSessionMessages(String sessionId) {
        List<ChatbotMessage> messages = chatbotMessageRepository.findBySessionIdOrderByTimestampAsc(sessionId);
        log.info("세션 {}의 메시지 조회 - {}개 메시지 반환", sessionId, messages.size());
        return messages;
    }

    // 4, 특정 유저의 모든 채팅 세션 불러오기
    @Transactional(readOnly = true)
    public List<ChatbotSession> getUserSessions(String userId) {
        User user = userService.findByUserId(userId);

        List<ChatbotSession> sessions = chatbotSessionRepository.findByUserSeqOrderByLastMessageAtDesc(user.getUserSeq());
        log.info("사용자 {}의 세션 조회 - {}개 세션 반환", userId, sessions.size());
        return sessions;
    }

    // 5. 채팅 세션 삭제하기
    @Transactional
    public void deleteSession(String userId, String sessionId) {
        User user = userService.findByUserId(userId);
        ChatbotSession session = findSessionById(sessionId);

        if (!(userService.validateAdmin(user) || session.getUserSeq().equals(user.getUserSeq()))) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        chatbotMessageRepository.deleteBySessionId(sessionId);
        chatbotSessionRepository.deleteById(sessionId);
        log.info("세션 {} 및 관련 메시지 삭제 완료", sessionId);
    }


    // 6. 채팅 세션 이름 수정
    @Transactional
    public void updateSessionTitle(String userId, String sessionId, String newTitle) {
        User user = userService.findByUserId(userId);

        ChatbotSession session = findSessionById(sessionId);
        if (!(userService.validateAdmin(user) || session.getUserSeq().equals(user.getUserSeq()))) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        session.updateTitle(newTitle);
        chatbotSessionRepository.save(session);
        log.info("세션 {}의 제목 수정 완료: {}", sessionId, newTitle);
    }

    public ChatbotSession findSessionById(String sessionId) {
        return chatbotSessionRepository.findById(sessionId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_CHATBOT_SESSION));
    }

}
