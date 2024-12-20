package mediHub_be.anonymousBoard.entity;

import jakarta.persistence.*;
import lombok.*;
import mediHub_be.common.aggregate.entity.BaseFullEntity;
import mediHub_be.user.entity.User;

@Entity
@Getter
@Table(name = "anonymous_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AnonymousBoard extends BaseFullEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anonymous_board_seq")
    private Long anonymousBoardSeq;                     // 익명 게시글 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;                                  // 작성자

    @Column(name = "anonymous_board_title")
    private String anonymousBoardTitle;                 // 제목

    @Column(name = "anonymous_board_content", columnDefinition = "LONGTEXT")
    private String anonymousBoardContent;               // 내용

    @Column(name = "anonymous_board_is_deleted")
    private Boolean anonymousBoardIsDeleted = false;    // 상태

    @Column(name = "anonymous_board_view_count")
    private Long anonymousBoardViewCount = 0L;          // 조회 수

    // 작성일은 BaseFullEntity에서 상속
    // 수정일은 BaseFullEntity에서 상속
    // 삭제일은 BaseFullEntity에서 상속

    public static AnonymousBoard createNewAnonymousBoard(
            User user,
            String anonymousBoardTitle,
            String anonymousBoardContent
    ) {

        return AnonymousBoard.builder()
                .user(user)
                .anonymousBoardTitle(anonymousBoardTitle)
                .anonymousBoardContent(anonymousBoardContent)
                .anonymousBoardIsDeleted(false)
                .anonymousBoardViewCount(0L)
                .build();
    }

    public void updateContent(String anonymousBoardTitle, String anonymousBoardContent) {

        this.anonymousBoardTitle = anonymousBoardTitle;
        this.anonymousBoardContent = anonymousBoardContent;
    }

    public void updateAnonymousBoardContent(String anonymousBoardTitle, String anonymousBoardContent) {

        this.anonymousBoardTitle = anonymousBoardTitle;
        this.anonymousBoardContent = anonymousBoardContent;
    }

    public void increaseViewCount() {

        if(this.anonymousBoardViewCount == null) {
            this.anonymousBoardViewCount = 1L;
        } else this.anonymousBoardViewCount++;
    }

    public void setDeleted() {

        this.anonymousBoardIsDeleted = true;

        delete();
    }
}