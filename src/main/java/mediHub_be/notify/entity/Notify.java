package mediHub_be.notify.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mediHub_be.board.entity.Flag;
import mediHub_be.common.aggregate.entity.CreateTimeEntity;
import mediHub_be.user.entity.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_seq")
    private Flag flag;

    @Enumerated(EnumType.STRING)
    @Column(name = "readed_at", nullable = false)
    @ColumnDefault(value = "'N'")
    private NotiReadStatus noti_isRead;

    @Column(name = "noti_content", nullable = false)
    private String notiContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "noti_type", nullable = false)
    private NotiType notiType;

    @Column(name = "noti_url", nullable = false)
    private String notiUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User receiver;

    @Column(name = "noti_sender_user_name", nullable = false)
    private String notiSenderUserName;

    @Column(name = "noti_sender_user_part", nullable = false)
    private String notiSenderUserPart;

    @Builder
    public Notify(User receiver, Flag flag, NotiType notiType,
                  String notiContent, String notiUrl, NotiReadStatus isRead,
                  String senderUserName, String senderUserPart) {
        this.receiver = receiver;
        this.flag = flag;
        this.notiType = notiType;
        this.notiContent = notiContent;
        this.notiUrl = notiUrl;
        this.noti_isRead = isRead;
        this.notiSenderUserName = senderUserName;
        this.notiSenderUserPart = senderUserPart;
    }

    // === 도메인 로직 === //

    // 알림 읽음
    public void notiRead(){
        this.noti_isRead = NotiReadStatus.Y;
    }

}
