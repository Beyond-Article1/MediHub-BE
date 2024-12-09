package mediHub_be.chatbot.repository;

import mediHub_be.chatbot.entity.ChatbotMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatbotMessageRepository extends MongoRepository<ChatbotMessage, String> {
    List<ChatbotMessage> findBySessionIdOrderByTimestampAsc(String sessionId);
    void deleteBySessionId(String sessionId);
}
