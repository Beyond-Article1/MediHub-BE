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

    private Long caseSharingBaseSeq;

    private String caseSharingTitle;

    private String caseSharingContent;

    @Builder
    public CaseSharing(User user, Part part, Template template, Long caseSharingBaseSeq, String caseSharingTitle, String caseSharingContent) {
        this.user = user;
        this.part = part;
        this.template = template;
        this.caseSharingBaseSeq = caseSharingBaseSeq;
        this.caseSharingTitle = caseSharingTitle;
        this.caseSharingContent = caseSharingContent;
    }

    // 빌더 패턴으로 baseSeq 설정
    public static CaseSharing createNewCaseSharing(User user, Part part, Template template, Long caseSharingBaseSeq, String title, String content) {
        return CaseSharing.builder()
                .user(user)
                .part(part)
                .template(template)
                .caseSharingBaseSeq(caseSharingBaseSeq) // baseSeq 설정
                .caseSharingTitle(title)
                .caseSharingContent(content)
                .build();
    }

}
