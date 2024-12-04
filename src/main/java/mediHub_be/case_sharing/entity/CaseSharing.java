package mediHub_be.case_sharing.entity;

import jakarta.persistence.*;
import lombok.*;
import mediHub_be.common.aggregate.entity.BaseFullEntity;
import mediHub_be.part.entity.Part;
import mediHub_be.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "case_sharing")
@ToString
public class CaseSharing extends BaseFullEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caseSharingSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_seq", nullable = false)
    private Part part;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_seq", nullable = false)
    private Template template;

    private String caseSharingTitle;

    private String caseSharingContent;

    @Builder
    public CaseSharing(User user, Part part, Template template, String caseSharingTitle, String caseSharingContent) {
        this.user = user;
        this.part = part;
        this.template = template;
        this.caseSharingTitle = caseSharingTitle;
        this.caseSharingContent = caseSharingContent;
    }

}
