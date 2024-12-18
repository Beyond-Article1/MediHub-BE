package mediHub_be.notify.dto;

import lombok.*;
import mediHub_be.common.utils.TimeFormatUtil;
import mediHub_be.notify.entity.NotiReadStatus;
import mediHub_be.notify.entity.NotiType;
import mediHub_be.notify.entity.Notify;
import mediHub_be.user.entity.User;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class NotifyDTO {

    Long notiSeq;
    // 받는 사람 이름
    String name;
    // 알림 내용 (댓글, MediLife, 케이스공유)
    String content;
    // 알림 타입
    NotiType type;
    // 알림 발생자 이름
    String senderName;
    // 알림 발생자의 과
    String partName;
    // 알림 발생된 게시글 유형 (MediLife, 익명게시판)
    String boardType;
    // 생성시간
    String createdAt;
    // 읽음 여부
    boolean isRead;

    public NotifyDTO(Notify notify){
        this.notiSeq = notify.getNotiSeq();
        this.name = notify.getReceiver().getUserName();
        this.content = notify.getNotiContent();
        this.type = notify.getNotiType();
        this.senderName = notify.getNotiSenderUserName();
        this.partName = notify.getNotiSenderUserPart();
        this.boardType = notify.getNotiType().getMessage();
        this.createdAt = notify.getCreatedAt().toString();
        this.isRead = notify.getNoti_isRead() == NotiReadStatus.Y;
    }

    public static NotifyDTO createResponse(Notify notify, String senderUserName, String senderUserPart) {
        return NotifyDTO.builder()
                .content(notify.getNotiContent())
                .notiSeq(notify.getNotiSeq())
                .name(notify.getReceiver().getUserName())
                .type(notify.getNotiType())
                .senderName(senderUserName)
                .partName(senderUserPart)
                .createdAt(TimeFormatUtil.yearAndMonthDayHMS(notify.getCreatedAt()).toString())
                .isRead(false)
                .build();
    }
}
