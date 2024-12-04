package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mediHub_be.common.aggregate.entity.CreateTimeEntity;
import mediHub_be.user.entity.User;

@Entity
@Table(name = "cp_version")
@Getter
@NoArgsConstructor
public class CpVersion extends CreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpVersionSeq;                      // cp 버전 번호

    @Column
    private long cpSeq;                             // cp 번호

    @Column
    private long userSeq;                           // 등록자

    @Column
    private String cpVersion;                       // cp 버전

    @Column
    private String cpVersionDescription;            // cp 버전 설명

    @Column
    private String cpUrl;                           // cp url

    public CpVersion(long cpSeq, long userSeq, String cpVersion, String cpVersionDescription, String cpUrl) {
        this.cpSeq = cpSeq;
        this.userSeq = userSeq;
        this.cpVersion = cpVersion;
        this.cpVersionDescription = cpVersionDescription;
        this.cpUrl = cpUrl;
    }
}
