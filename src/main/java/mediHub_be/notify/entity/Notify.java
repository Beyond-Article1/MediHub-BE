package mediHub_be.notify.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mediHub_be.common.aggregate.entity.CreateTimeEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@NoArgsConstructor
public class Notify extends CreateTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noti_seq", nullable = false)
    private Long notiSeq;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_read", nullable = false)
    @ColumnDefault(value = "'N'")
    private NotiReadStatus isRead;

    @Column(name = "noti_content", nullable = false)
    private String notiContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "noti_type", nullable = false)
    private NotiType notiType;

    @Column(name = "noti_url", nullable = false)
    private String notiUrl;

//    @Column(name = "user_seq")
//    private Long receiver;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User receiver;

    @Builder
    public Notify(User receiver, NotiType notiType, String notiContent, String notiUrl, NotiReadStatus isRead) {
        this.receiver = receiver;
        this.notiType = notiType;
        this.notiContent = notiContent;
        this.notiUrl = notiUrl;
        this.isRead = isRead;
    }

    // === 도메인 로직 === //

    // 알림 읽음
    public void notiRead(){
        this.isRead = NotiReadStatus.Y;
    }

    public enum NotiType {
        BOARD,  // 팔로우 한 사람의 새로운 게시글
        COMMENT,    // 내 게시글에 대한 댓글
        CASE    // 팔로우 한 사람의 새로운 케이스공유 글
    }

    public enum NotiReadStatus {
        Y,  // 읽음
        N   // 안읽음
    }

}
