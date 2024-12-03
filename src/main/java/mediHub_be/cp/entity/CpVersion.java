package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import mediHub_be.common.aggregate.entity.CreateTimeEntity;

@Entity
@Table(name = "cp_version")
@Getter
public class CpVersion extends CreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpVersionSeq;                       // cp 버전 번호

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
}
