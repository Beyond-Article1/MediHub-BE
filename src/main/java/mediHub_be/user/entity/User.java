package mediHub_be.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE user SET user_state = 'DELETE', del_date = LOCALTIME WHERE user_seq = ?")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private long userSeq;

    @Column(name = "part_seq", nullable = true)
    private long partSeq;

    @Column(name = "ranking_seq", nullable = true)
    private long rankingSeq;

    @Column(name = "picture_seq", nullable = true)
    private long pictureSeq;

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

    public User(String userId, String userPassword, String userName, String userEmail, String userPhone) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
    }

    public void encryptPassword(String encodedPwd) {
        this.userPassword= encodedPwd;
    }

}




