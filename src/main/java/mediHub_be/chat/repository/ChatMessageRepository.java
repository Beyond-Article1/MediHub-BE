package mediHub_be.chat.repository;

import mediHub_be.chat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    ChatMessage findTopByChatroomSeqAndIsDeletedFalseOrderByCreatedAtDesc(Long chatroomSeq);

    List<ChatMessage> findByChatroomSeqAndCreatedAtAfterAndIsDeletedFalseOrderByCreatedAtAsc(Long chatroomSeq, LocalDateTime userJoinDate);

    Long countByChatroomSeqAndCreatedAtAfterAndIsDeletedFalse(Long chatroomSeq, LocalDateTime lastVisitedAt);
}

