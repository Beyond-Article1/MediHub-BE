package mediHub_be.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mediHub_be.common.aggregate.entity.BaseFullEntity;

@Entity
@Table(name = "chatroom")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatroom extends BaseFullEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatroomSeq;

    private String chatroomDefaultName;

    @Builder
    public Chatroom(Long chatroomSeq, String chatroomDefaultName) {
        this.chatroomSeq = chatroomSeq;
        this.chatroomDefaultName = chatroomDefaultName;
    }

    public void updateChatroomDefaultName(String chatroomDefaultName) {
        this.chatroomDefaultName = chatroomDefaultName;
    }

}
