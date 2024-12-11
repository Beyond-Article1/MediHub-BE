package mediHub_be.follow.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mediHub_be.user.entity.User;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_seq", nullable = false)
    private Long followSeq;

    // 팔로워
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_from_seq", nullable = false)
    private User userFrom;

    // 팔로잉
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_to_seq", nullable = false)
    private User userTo;

    public Follow(User userFrom, User userTo) {
        this.userFrom = userFrom;
        this.userTo = userTo;
    }
}
