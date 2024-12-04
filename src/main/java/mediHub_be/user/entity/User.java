package mediHub_be.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
//import mediHub_be.case_sharing.entity.Picture;
import mediHub_be.part.entity.Part;
import mediHub_be.ranking.entity.Ranking;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_seq", nullable = false)
    private Part part;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ranking_seq", nullable = false)
    private Ranking ranking;


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "picture_seq", nullable = false)
//    private Picture picture;

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

    public void encryptPassword(String encodedPwd) {
        this.userPassword= encodedPwd;
    }

}




