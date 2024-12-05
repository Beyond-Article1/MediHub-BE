package mediHub_be.notify.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "noti_is_read", nullable = false)
    @ColumnDefault(value = "'N'")
    private NotiReadStatus noti_isRead;

    @Column(name = "noti_content", nullable = false)
    private String notiContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "noti_type", nullable = false)
    private NotiType notiType;

    @Column(name = "noti_url", nullable = false)
    private String notiUrl;

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
        this.noti_isRead = isRead;
    }

    // === 도메인 로직 === //

    // 알림 읽음
    public void notiRead(){
        this.noti_isRead = NotiReadStatus.Y;
    }

}
