package mediHub_be.chatbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.chatbot.entity.ChatbotMessage;
import mediHub_be.chatbot.entity.ChatbotSession;
import mediHub_be.chatbot.repository.ChatbotMessageRepository;
import mediHub_be.chatbot.repository.ChatbotSessionRepository;
import mediHub_be.common.utils.DateTimeUtil;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotRestService {
    private final ChatbotSessionRepository chatbotSessionRepository;
    private final ChatbotMessageRepository chatbotMessageRepository;
    private final UserRepository userRepository;

    // 1. 새로운 채팅 세션 생성
    public String createNewChatSession(String userId, String title) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

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
    public void saveMessage(String userId, String sessionId, String sender, String content) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

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
    public List<ChatbotMessage> getSessionMessages(String sessionId) {
        List<ChatbotMessage> messages = chatbotMessageRepository.findBySessionIdOrderByTimestampAsc(sessionId);
        log.info("세션 {}의 메시지 조회 - {}개 메시지 반환", sessionId, messages.size());
        return messages;
    }

    // 4, 특정 유저의 모든 채팅 세션 불러오기
    public List<ChatbotSession> getUserSessions(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        List<ChatbotSession> sessions = chatbotSessionRepository.findByUserSeqOrderByLastMessageAtDesc(user.getUserSeq());
        log.info("사용자 {}의 세션 조회 - {}개 세션 반환", userId, sessions.size());
        return sessions;
    }

    // 5. 채팅 세션 삭제하기
    public void deleteSession(String userId, String sessionId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        ChatbotSession session = chatbotSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 세션입니다."));

        if (!session.getUserSeq().equals(user.getUserSeq())) {
            throw new IllegalArgumentException("세션 삭제 권한이 없습니다.");
        }

        chatbotMessageRepository.deleteBySessionId(sessionId);
        chatbotSessionRepository.deleteById(sessionId);
        log.info("세션 {} 및 관련 메시지 삭제 완료", sessionId);
    }


    // 6. 채팅 세션 이름 수정
    public void updateSessionTitle(String userId, String sessionId, String newTitle) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        ChatbotSession session = chatbotSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 세션입니다."));

        if (!session.getUserSeq().equals(user.getUserSeq())) {
            throw new IllegalArgumentException("세션 수정 권한이 없습니다.");
        }

        session.updateTitle(newTitle);
        chatbotSessionRepository.save(session);
        log.info("세션 {}의 제목 수정 완료: {}", sessionId, newTitle);
    }

}
