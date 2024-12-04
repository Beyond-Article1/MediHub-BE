package mediHub_be.case_sharing.entity;

import jakarta.persistence.*;
import lombok.*;
import mediHub_be.common.aggregate.entity.BaseFullEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "version")
@ToString
public class Version extends BaseFullEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long versionSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_sharing_seq", nullable = false)
    private CaseSharing caseSharing;

    private Integer versionNum;

    private boolean versionIsLatest;

    @Builder
    public Version(CaseSharing caseSharing, int versionNum, boolean versionIsLatest) {
        this.caseSharing = caseSharing;
        this.versionNum = versionNum;
        this.versionIsLatest = versionIsLatest;
    }

}
