package mediHub_be.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.service.PictureService;
import mediHub_be.chat.dto.ChatroomDTO;
import mediHub_be.chat.dto.ResponseChatUserDTO;
import mediHub_be.chat.dto.ResponseChatroomDTO;
import mediHub_be.chat.dto.UpdateChatroomDTO;
import mediHub_be.chat.entity.Chat;
import mediHub_be.chat.entity.ChatMessage;
import mediHub_be.chat.entity.Chatroom;
import mediHub_be.chat.repository.ChatMessageRepository;
import mediHub_be.chat.repository.ChatRepository;
import mediHub_be.chat.repository.ChatroomRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.part.entity.Part;
import mediHub_be.ranking.entity.Ranking;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import mediHub_be.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final UserService userService;
    private final PictureService pictureService;
    private final ChatroomRepository chatroomRepository;
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;

    /* 채팅방 생성 */
    @Transactional
    public Long createChatroom(ChatroomDTO chatroomDTO) {

        Chatroom chatroom = Chatroom.builder()
                .chatroomDefaultName("채팅방")
                .build();
        Chatroom savedChatroom = chatroomRepository.save(chatroom); // 채팅방 저장 후 생성된 chatGroupSeq를 반환
        Long chatroomSeq = savedChatroom.getChatroomSeq();

        // Chat 테이블에 사용자 정보 추가, 채팅방 기본 이름 설정
        StringBuilder usersInChatGroup = new StringBuilder();
        for (Long userSeq : chatroomDTO.getUsers()) {

            User user = userService.findUser(userSeq);
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
            // 이미 채팅방에 참여 중인지 확인
            if (chatRepository.existsByChatroomChatroomSeqAndUserUserSeq(chatroomSeq, userSeq)) {
                throw new CustomException(ErrorCode.USER_ALREADY_IN_CHATROOM);
            }

            // 사용자 조회
            User user = userService.findUser(userSeq);
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
    @Transactional(readOnly = true)
    public List<ResponseChatroomDTO> getChatroomListByUserSeq(Long userSeq) {
        List<Chat> chats = chatRepository.findByUser_UserSeq(userSeq);
        log.info("사용자 {}의 채팅방 조회 - {}개 채팅방 반환", userSeq, chats.size());

        // 각 채팅방에 대한 정보를 DTO로 매핑
        return chats.stream()
                .map(chat -> {
                    // 채팅방 정보 가져오기
                    Chatroom chatroom = chat.getChatroom();
                    // 해당 채팅방에 참여한 사용자 수
                    Long chatroomUsersCount = chatRepository.countByChatroom_ChatroomSeq(chatroom.getChatroomSeq());

                    // 마지막 메시지 조회 (최근 메시지 1개만 가져오기)
                    ChatMessage lastMessage = chatMessageRepository.findTopByChatroomSeqAndIsDeletedFalseOrderByCreatedAtDesc(chatroom.getChatroomSeq());
                    String lastMessageText = (lastMessage != null) ? lastMessage.getMessage() : "No messages yet";
                    LocalDateTime lastMessageTime = (lastMessage != null) ? lastMessage.getCreatedAt().minusHours(9) : null;

                    // ResponseChatroomDTO로 변환
                    return ResponseChatroomDTO.builder()
                            .chatroomSeq(chatroom.getChatroomSeq())                 // 채팅방 Seq
                            .chatroomDefaultName(chatroom.getChatroomDefaultName()) // 채팅방 기본 이름
                            .chatroomCustomName(chat.getChatroomCustomName())       // 사용자별 채팅방 설정 이름
                            .chatroomUsersCount(chatroomUsersCount)                 // 채팅방 사용자 수
                            .lastMessage(lastMessageText)                           // 마지막 메시지
                            .lastMessageTime(lastMessageTime)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /* 채팅방 정보 조회 */
    @Transactional(readOnly = true)
    public ResponseChatroomDTO getChatroomInfoBySeq(Long userSeq, Long chatroomSeq) {
        Chat chat = chatRepository.findByUser_UserSeqAndChatroom_ChatroomSeq(userSeq, chatroomSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));
        Chatroom chatroom = chat.getChatroom();
        Long chatroomUsersCount = chatRepository.countByChatroom_ChatroomSeq(chatroom.getChatroomSeq());

        return ResponseChatroomDTO.builder()
                .chatroomSeq(chatroom.getChatroomSeq())
                .chatroomDefaultName(chatroom.getChatroomDefaultName())
                .chatroomCustomName(chat.getChatroomCustomName())
                .chatroomUsersCount(chatroomUsersCount)
                .build();
    }

    /* 특정 채팅방 참여자 조회 */
    @Transactional(readOnly = true)
    public List<ResponseChatUserDTO> getChatUsers(Long userSeq, Long chatroomSeq) {
        // 조회하려는 사용자가 해당 채팅방에 존재하지 않으면 권한 없음 예외 발생
        if(!chatRepository.findByUser_UserSeqAndChatroom_ChatroomSeq(userSeq, chatroomSeq).isPresent()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        List<Chat> chats = chatRepository.findByChatroom_ChatroomSeq(chatroomSeq);

        return chats.stream()
                .map(chat -> {
                    User user = chat.getUser();
                    Part part = user.getPart();
                    Ranking ranking = user.getRanking();
                    String profileUrl = pictureService.getUserProfileUrl(user.getUserSeq());

                    return ResponseChatUserDTO.builder()
                            .userSeq(user.getUserSeq())
                            .userName(user.getUserName())
                            .userProfileUrl(profileUrl)
                            .partName(part != null ? part.getPartName() : "N/A")
                            .rankingName(ranking != null ? ranking.getRankingName() : "N/A")
                            .build();
                })
                .collect(Collectors.toList());
    }
}
