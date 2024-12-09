package mediHub_be.chat.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mediHub_be.chat.dto.ChatroomDTO;
import mediHub_be.chat.dto.ChatroomInfoDTO;
import mediHub_be.chat.dto.UpdateChatroomDTO;
import mediHub_be.chat.entity.Chat;
import mediHub_be.chat.entity.Chatroom;
import mediHub_be.chat.repository.ChatRepository;
import mediHub_be.chat.repository.ChatroomRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatRepository chatRepository;

    /* 채팅방 생성 */
    @Transactional
    public Long createChatroom(ChatroomDTO chatroomDTO) {

        Chatroom chatroom = Chatroom.builder().build();
        Chatroom savedChatroom = chatroomRepository.save(chatroom); // 채팅방 저장 후 생성된 chatGroupSeq를 반환
        Long chatroomSeq = savedChatroom.getChatroomSeq();

        // Chat 테이블에 사용자 정보 추가, 채팅방 기본 이름 설정
        StringBuilder usersInChatGroup = new StringBuilder();
        for (Long userSeq : chatroomDTO.getUsers()) {
            User user = userRepository.findByUserSeq(userSeq)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

            usersInChatGroup.append(user.getUserName()).append(" ");    // 채팅방 참여자 이름 나열

            Chat chat = Chat.builder()
                    .user(user)
                    .chatroom(savedChatroom)
                    .build();

            chatRepository.save(chat);   // 채팅방 참여자를 Chat 테이블에 추가
        }

        String chatroomDefaultName = usersInChatGroup.toString().trim();
        savedChatroom.updateChatroomDefaultName(chatroomDefaultName);

        return chatroomSeq;
    }

    /* 대화상대 초대(추가) */
    @Transactional
    public void updateChatroomMember(Long chatroomSeq, ChatroomDTO chatroomDTO) {
        Chatroom chatroom = chatroomRepository.findByChatroomSeq(chatroomSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));

        StringBuilder usersInChatGroup = new StringBuilder();
        usersInChatGroup.append(chatroom.getChatroomDefaultName()).append(" ");

        for (Long userSeq : chatroomDTO.getUsers()) {
            User user = userRepository.findByUserSeq(userSeq)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

            usersInChatGroup.append(user.getUserName()).append(" ");

            Chat chat = Chat.builder()
                    .user(user)
                    .chatroom(chatroom)
                    .build();

            chatRepository.save(chat);
        }

        String chatroomDefaultName = usersInChatGroup.toString().trim();
        chatroom.updateChatroomDefaultName(chatroomDefaultName);

    }

    // 사용자별 채팅방 이름 변경(설정)
    @Transactional
    public void updateChatroomName(Long userSeq, Long chatroomSeq, UpdateChatroomDTO updatechatroomDTO) {
        Chat chat = chatRepository.findByUser_UserSeqAndChatroom_ChatroomSeq(userSeq, chatroomSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));
        chat.updateChatroomName(updatechatroomDTO.getChatroomCustomName());
        chatRepository.save(chat);
    }

    /* 채팅방 나가기 */
    @Transactional
    public void deleteChatroom(Long userSeq, Long chatroomSeq) {
        Chat chat = chatRepository.findByUser_UserSeqAndChatroom_ChatroomSeq(userSeq, chatroomSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));
        chatRepository.delete(chat);

        // 해당 채팅방에 남아있는 사용자가 있는지 확인
        long remainingUsersCount = chatRepository.countByChatroom_ChatroomSeq(chatroomSeq);

        if (remainingUsersCount > 0) {
            // 채팅방 기본 이름 갱신
            List<Chat> remainingChats = chatRepository.findByChatroom_ChatroomSeq(chatroomSeq);
            StringBuilder usersInChatGroup = new StringBuilder();
            for (Chat remainingChat : remainingChats) {
                usersInChatGroup.append(remainingChat.getUser().getUserName()).append(" ");
            }
            String updatedChatroomDefaultName = usersInChatGroup.toString().trim();
            Chatroom chatroom = chatroomRepository.findById(chatroomSeq)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));

            // 채팅방 이름 업데이트
            chatroom.updateChatroomDefaultName(updatedChatroomDefaultName);
            chatroomRepository.save(chatroom);
        } else {
            // 채팅방에 더 이상 사용자가 없으면 채팅방을 soft delete 처리
            Chatroom chatroom = chatroomRepository.findById(chatroomSeq)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));
            chatroom.delete();
            chatroomRepository.save(chatroom);
        }

    }

    /* 사용자별 채팅 목록 조회 */
    public List<ChatroomInfoDTO> getChatroomListByUserSeq(Long userSeq) {
        return null;
    }

    /* 채팅방 정보 조회 */
    public ChatroomInfoDTO getChatroomInfoBySeq(Long chatroomSeq) {
        return null;
    }

}
