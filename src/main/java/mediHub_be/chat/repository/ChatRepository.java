package mediHub_be.chat.repository;

import mediHub_be.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findByUser_UserSeqAndChatroom_ChatroomSeq(Long userSeq, Long chatroomSeq);

    long countByChatroom_ChatroomSeq(Long chatroomSeq);

    List<Chat> findByChatroom_ChatroomSeq(Long chatroomSeq);

    List<Chat> findByUser_UserSeq(Long userSeq);
}

