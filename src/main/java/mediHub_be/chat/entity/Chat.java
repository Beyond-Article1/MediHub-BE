package mediHub_be.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mediHub_be.user.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_seq", nullable = false)
    private Chatroom chatroom;

    private String chatroomCustomName;

    @CreatedDate
    private LocalDateTime joinedAt;

    @Builder
    public Chat(User user, Chatroom chatroom, String chatroomCustomName) {
        this.user = user;
        this.chatroom = chatroom;
        this.chatroomCustomName = chatroomCustomName;
    }

    public void updateChatroomName(String chatroomCustomName) {
        this.chatroomCustomName = chatroomCustomName;
    }
}
