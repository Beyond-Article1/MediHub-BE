package mediHub_be.chat.repository;

import mediHub_be.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT c.chatroom.chatroomSeq " +
            "FROM Chat c " +
            "GROUP BY c.chatroom.chatroomSeq " +
            "HAVING COUNT(DISTINCT c.user.userSeq) = 2 " +  // 정확히 2명이 있어야 함
            "   AND COUNT(DISTINCT CASE WHEN c.user.userSeq IN (:userSeqs) THEN c.user.userSeq END) = 2 " +  // 제공된 userSeqs만 참여한 채팅방 찾기
            "ORDER BY MAX(c.joinedAt) DESC")
    List<Long> findExistingChatroom(@Param("userSeqs") List<Long> userSeqs);

    // 사용자의 각 채팅방 참여 정보 (chatroomSeq, joinedAt) 조회
    interface ChatroomJoinInfo {
        Long getChatroomSeq();
        LocalDateTime getJoinedAt();
    }

    @Query("SELECT c.chatroom.chatroomSeq AS chatroomSeq, c.joinedAt AS joinedAt " +
            "FROM Chat c WHERE c.user.userSeq = :userSeq")
    List<ChatroomJoinInfo> findJoinInfoByUserSeq(@Param("userSeq") Long userSeq);
}
