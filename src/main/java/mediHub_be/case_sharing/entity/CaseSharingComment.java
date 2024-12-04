package mediHub_be.case_sharing.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mediHub_be.common.aggregate.entity.BaseFullEntity;
import mediHub_be.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

}
