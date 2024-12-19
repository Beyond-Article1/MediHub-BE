package mediHub_be.board.entity;

import jakarta.persistence.*;
import lombok.*;
import mediHub_be.common.aggregate.entity.BaseFullEntity;
import mediHub_be.user.entity.User;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseFullEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_seq")
    private Long commentSeq;                    // 댓글 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_seq", nullable = false)
    private User user;                          // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="flag_seq", nullable = false)
    private Flag flag;                          // 식별 번호

    private String commentContent;              // 내용
    private Boolean commentIsDeleted = false;   // 상태

    // 작성일은 BaseFullEntity에서 상속
    // 수정일은 BaseFullEntity에서 상속
    // 삭제일은 BaseFullEntity에서 상속

    public static Comment createNewComment(
            User user,
            Flag flag,
            String commentContent) {

        return Comment.builder()
                .user(user)
                .flag(flag)
                .commentContent(commentContent)
                .commentIsDeleted(false)
                .build();
    }

    public void update(User user, Flag flag, String commentContent) {

        this.user = user;
        this.flag = flag;
        this.commentContent = commentContent;
    }

    public void updateContent(Flag flag, String commentContent) {

        this.flag = flag;
        this.commentContent = commentContent;
    }

    public void setDeleted() {

        this.commentIsDeleted = true;

        delete();
    }
}