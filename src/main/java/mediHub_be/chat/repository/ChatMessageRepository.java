package mediHub_be.chat.repository;

import mediHub_be.chat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByChatroomSeqAndIsDeletedFalseOrderByCreatedAtAsc(Long chatroomSeq);

    ChatMessage findTopByChatroomSeqAndIsDeletedFalseOrderByCreatedAtDesc(Long chatroomSeq);
}

