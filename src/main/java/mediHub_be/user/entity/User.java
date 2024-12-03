package mediHub_be.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import mediHub_be.common.aggregate.entity.BaseFullEntity;

@Entity
@Table(name = "user")
@Getter
public class User extends BaseFullEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userSeq;                               // 직원 번호

    @Column
    private long classSeq;                              // 과

    @Column
    private String rankSeq;                             // 직급

    @Column
    private long pictureSeq;                            // 프로필사진

    @Column
    private String userName;                            // 직원명

    @Column
    private String userId;                              // 아이디

    @Column
    private String userPassword;                        // 비밀번호

    @Column
    private String userEmail;                           // 이메일

    @Column
    private String userPhone;                           // 연락처

    @Column
    @Enumerated(EnumType.STRING)
    private UserAuth userAuth = UserAuth.USER;          // 권한

    @Column
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.ACTIVE;  // 상태
}
