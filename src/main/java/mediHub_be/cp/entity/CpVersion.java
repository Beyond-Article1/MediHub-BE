package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import mediHub_be.common.aggregate.entity.CreateTimeEntity;
import mediHub_be.user.entity.User;

@Entity
@Table(name = "cp_version")
@Getter
public class CpVersion extends CreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpVersionSeq;                      // cp 버전 번호

    @ManyToOne
    @JoinColumn(name = "cp_seq")
    private Cp cpSeq;                               // cp 번호

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private User userSeq;                           // 등록자

    @Column
    private String cpVersion;                       // cp 버전

    @Column
    private String cpVersionDescription;            // cp 버전 설명

    @Column
    private String cpUrl;                           // cp url
}
