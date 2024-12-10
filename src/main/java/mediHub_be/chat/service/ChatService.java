package mediHub_be.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.chat.dto.ChatMessageDTO;
import mediHub_be.chat.dto.ResponseChatMessageDTO;
import mediHub_be.chat.entity.ChatMessage;
import mediHub_be.chat.repository.ChatMessageRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    /* 채팅 메시지 저장 */
    public ResponseChatMessageDTO saveMessage(ChatMessageDTO message) {

        ChatMessage newMessage = ChatMessage.builder()
                .chatroomSeq(message.getChatroomSeq())
                .senderUserSeq(message.getSenderUserSeq())
                .type(message.getType())
                .message(message.getMessage())
                .createdAt(message.getCreatedAt())
                .attachments(message.getAttachments())
                .build();
        chatMessageRepository.save(newMessage);

        // ChatMessage -> ResponseChatMessageDTO 변환
        User senderUser = userRepository.findByUserSeq(newMessage.getSenderUserSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return ResponseChatMessageDTO.builder()
                .messageSeq(newMessage.getId())
                .chatroomSeq(newMessage.getChatroomSeq())
                .senderUserSeq(newMessage.getSenderUserSeq())
                .senderUserName(senderUser.getUserName())
                .type(newMessage.getType())
                .message(newMessage.getMessage())
                .createdAt(newMessage.getCreatedAt().minusHours(9))
                .attachments(message.getAttachments())
                .build();

    }

    /* 채팅 메시지 삭제 */
    @Transactional
    public void deleteMessage(String messageId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATMESSAGE));
        // 논리삭제
        message.setIsDeleted(true);
        chatMessageRepository.save(message);
    }

    /* 특정 채팅방의 메시지 조회 */
    public List<ResponseChatMessageDTO> getMessagesByRoomSeq(Long chatroomSeq) {
        List<ChatMessage> messages = chatMessageRepository.findByChatroomSeqAndIsDeletedFalseOrderByCreatedAtAsc(chatroomSeq);
        log.info("채팅방 {}의 메세지 조회 - {}개 메시지 반환", chatroomSeq, messages.size());

        // 모든 senderUserSeq를 추출하여 사용자 정보 한 번에 조회
        List<Long> userSeqs = messages.stream()
                .map(ChatMessage::getSenderUserSeq)
                .distinct()  // 중복 제거
                .collect(Collectors.toList());

        List<User> users = userRepository.findByUserSeqIn(userSeqs);

        // 사용자 정보를 Map으로 변환
        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getUserSeq, user -> user));

        // 메시지를 변환할 때 사용자 정보를 조회
        return messages.stream()
                .map(message -> {
                    User senderUser = userMap.get(message.getSenderUserSeq()); // 미리 조회한 사용자 정보 사용
                    return ResponseChatMessageDTO.builder()
                            .messageSeq(message.getId())
                            .chatroomSeq(message.getChatroomSeq())
                            .senderUserSeq(message.getSenderUserSeq())
                            .senderUserName(senderUser != null ? senderUser.getUserName() : "Unknown") // 사용자 이름 추가
                            .type(message.getType())
                            .message(message.getMessage())
                            .createdAt(message.getCreatedAt().minusHours(9))  // 시간대 조정
                            .attachments(message.getAttachments())
                            .build();
                })
                .collect(Collectors.toList());

    }

}
