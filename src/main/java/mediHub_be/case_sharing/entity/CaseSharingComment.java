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

    private String caseSharingBlockId;

    public void updateComment(String content, String caseSharingBlockId) {
        this.caseSharingCommentContent = content;
        this.caseSharingBlockId = caseSharingBlockId;
    }

    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
    }
}
