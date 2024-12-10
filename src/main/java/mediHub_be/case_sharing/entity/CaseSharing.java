package mediHub_be.case_sharing.entity;

import jakarta.persistence.*;
import lombok.*;
import mediHub_be.common.aggregate.entity.BaseCreateDeleteEntity;
import mediHub_be.common.aggregate.entity.BaseFullEntity;
import mediHub_be.part.entity.Part;
import mediHub_be.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "case_sharing")
@ToString
public class CaseSharing extends BaseCreateDeleteEntity {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_sharing_group_seq", nullable = false)
    private CaseSharingGroup caseSharingGroup;

    private String caseSharingTitle;

    @Column(columnDefinition = "LONGTEXT")
    private String caseSharingContent;

    private Boolean caseSharingIsDraft; // 임시저장 여부

    private Boolean caseSharingIsLatest; // 최신버전 여부

    private Long caseSharingViewCount = 0L; // 조회수

    @Builder
    public CaseSharing(User user, Part part, Template template, CaseSharingGroup caseSharingGroup,
                       String caseSharingTitle, String caseSharingContent, Boolean caseSharingIsDraft,
                       Boolean caseSharingIsLatest, Long caseSharingViewCount) {
        this.user = user;
        this.part = part;
        this.template = template;
        this.caseSharingGroup = caseSharingGroup;
        this.caseSharingTitle = caseSharingTitle;
        this.caseSharingContent = caseSharingContent;
        this.caseSharingIsDraft = caseSharingIsDraft;
        this.caseSharingIsLatest = caseSharingIsLatest;
        this.caseSharingViewCount = caseSharingViewCount;
    }

    // 새 CaseSharing 생성
    public static CaseSharing createNewCaseSharing(User user, Part part, Template template, CaseSharingGroup caseSharingGroup, String title, String content, Boolean isDraft) {
        return CaseSharing.builder()
                .user(user)
                .part(part)
                .template(template)
                .caseSharingGroup(caseSharingGroup)
                .caseSharingTitle(title)
                .caseSharingContent(content)
                .caseSharingIsDraft(isDraft)
                .caseSharingIsLatest(true) // 새로 생성된 경우 최신 버전으로 설정
                .caseSharingViewCount(0L)
                .build();
    }
    public void markAsLatest() {
        this.caseSharingIsLatest = true;
    }
    public void markAsNotLatest() {
        this.caseSharingIsLatest = false;
    }
    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
        this.caseSharingIsLatest = false; // 삭제된 경우 최신 버전이 될 수 없음
    }

    public void updateContent(String title, String content) {
        this.caseSharingTitle = title;
        this.caseSharingContent = content;
    }

    public void increaseViewCount() {
        if (this.caseSharingViewCount == null) {
            this.caseSharingViewCount = 1L;
        } else {
            this.caseSharingViewCount++;
        }
    }
}
