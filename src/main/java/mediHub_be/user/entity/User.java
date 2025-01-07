package mediHub_be.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mediHub_be.common.aggregate.entity.BaseFullEntity;
import mediHub_be.part.entity.Part;
import mediHub_be.ranking.entity.Ranking;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE user SET user_state = 'DELETE', deleted_at = LOCALTIME WHERE user_seq = ?")
public class User extends BaseFullEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_seq", nullable = false)
    private Part part;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ranking_seq", nullable = false)
    private Ranking ranking;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "user_phone", nullable = false)
    private String userPhone;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    private UserAuth userAuth = UserAuth.USER;

    @Builder
    public User(String userId, String userPassword, String userName, String userEmail, String userPhone, Part part, Ranking ranking, UserAuth userAuth, UserStatus userStatus) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.part = part;
        this.ranking = ranking;
        this.userAuth = userAuth != null ? userAuth : UserAuth.USER;
        this.userStatus = userStatus != null ? userStatus : UserStatus.ACTIVE;
    }

    public void initializePassword(String encodedPassword) {
        this.userPassword = encodedPassword;
    }

    public void updateUserDetails(Long userSeq, String userEmail, String userPhone, Part part, Ranking ranking, UserAuth userAuth, UserStatus userStatus) {
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.part = part;
        this.ranking = ranking;
        this.userAuth = userAuth;
        this.userStatus = userStatus;
    }

    public void markAsDeleted() {
        this.userStatus = UserStatus.DELETE;
    }

    public void updateUserinfo(String userEmail, String userPhone, String userPassword) {
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userPassword = userPassword;
    }

}




