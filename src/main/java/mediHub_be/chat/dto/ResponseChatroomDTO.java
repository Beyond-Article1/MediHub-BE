package mediHub_be.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseChatroomDTO {
    private Long chatroomSeq;           // chatroom seq값
    private String chatroomDefaultName; // 채팅방 기본 이름
    private String chatroomCustomName;  // 채팅방 설정 이름
    private Long chatroomUsersCount;    // 채팅방 참여 인원
    private String lastMessage;         // 마지막 채팅 내용
    private LocalDateTime lastMessageTime;     // 마지막 채팅 시간
}
