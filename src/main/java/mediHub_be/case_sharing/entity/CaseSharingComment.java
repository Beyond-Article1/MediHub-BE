package mediHub_be.case_sharing.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mediHub_be.common.aggregate.entity.BaseFullEntity;
import mediHub_be.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "case_sharing_comment")
@ToString
public class CaseSharingComment extends BaseFullEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caseSharingCommentSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_sharing_seq", nullable = false)
    private CaseSharing caseSharing;

    private String caseSharingCommentContent;

    private Integer caseSharingCommentStartOffset;

    private Integer caseSharingCommentEndOffset;

    // 빌더 메서드를 통해 객체 생성
    @Builder
    public CaseSharingComment(User user, CaseSharing caseSharing, String caseSharingCommentContent,
                              Integer caseSharingCommentStartOffset, Integer caseSharingCommentEndOffset) {
        this.user = user;
        this.caseSharing = caseSharing;
        this.caseSharingCommentContent = caseSharingCommentContent;
        this.caseSharingCommentStartOffset = caseSharingCommentStartOffset;
        this.caseSharingCommentEndOffset = caseSharingCommentEndOffset;
    }

    public void updateComment(String content, Integer startOffset, Integer endOffset) {
        this.caseSharingCommentContent = content;
        this.caseSharingCommentStartOffset = startOffset;
        this.caseSharingCommentEndOffset = endOffset;
    }

    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
    }
}
