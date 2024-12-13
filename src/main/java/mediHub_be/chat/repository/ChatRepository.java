package mediHub_be.chat.repository;

import mediHub_be.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findByUser_UserSeqAndChatroom_ChatroomSeq(Long userSeq, Long chatroomSeq);

    long countByChatroom_ChatroomSeq(Long chatroomSeq);

    List<Chat> findByChatroom_ChatroomSeq(Long chatroomSeq);

    List<Chat> findByUser_UserSeq(Long userSeq);

    @Query("SELECT c.joinedAt FROM Chat c WHERE c.user.userSeq = :userSeq AND c.chatroom.chatroomSeq = :chatroomSeq")
    LocalDateTime findJoinDateByUserSeqAndChatroomSeq(Long userSeq, Long chatroomSeq);

    boolean existsByChatroomChatroomSeqAndUserUserSeq(Long chatroomSeq, Long userSeq);
}

