package mediHub_be.case_sharing.entity;

import jakarta.persistence.*;
import lombok.*;
import mediHub_be.common.aggregate.entity.BaseFullEntity;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "case_sharing_group")
@ToString
public class CaseSharingGroup extends BaseFullEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caseSharingGroupSeq;

    @OneToMany(mappedBy = "caseSharingGroup",
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CaseSharing> caseSharings;

    public static CaseSharingGroup createNewGroup() {
        return new CaseSharingGroup();
    }

    public CaseSharing getLatestVersion() {
        return caseSharings.stream()
                .filter(CaseSharing::getCaseSharingIsLatest)
                .findFirst()
                .orElse(null);
    }

}
