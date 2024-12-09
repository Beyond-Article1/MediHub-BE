package mediHub_be.chatbot.repository;

import mediHub_be.chatbot.entity.ChatbotSession;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatbotSessionRepository extends MongoRepository<ChatbotSession, String> {
    List<ChatbotSession> findByUserSeqOrderByLastMessageAtDesc(Long userSeq);
}

