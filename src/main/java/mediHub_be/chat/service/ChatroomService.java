package mediHub_be.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.service.PictureService;
import mediHub_be.chat.dto.*;
import mediHub_be.chat.entity.Chat;
import mediHub_be.chat.entity.ChatMessage;
import mediHub_be.chat.entity.Chatroom;
import mediHub_be.chat.repository.ChatMessageRepository;
import mediHub_be.chat.repository.ChatRepository;
import mediHub_be.chat.repository.ChatroomRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.notify.service.NotifyServiceImlp;
import mediHub_be.part.entity.Part;
import mediHub_be.ranking.entity.Ranking;
import mediHub_be.user.entity.User;
import mediHub_be.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final NotifyServiceImlp notifyServiceImlp;
    private final KafkaProducerService kafkaProducerService;

    /* 채팅방 생성 */
    @Transactional
    public Long createChatroom(Long myUserSeq, ChatroomDTO chatroomDTO) {

        List<Long> allUserSeqs = new ArrayList<>(chatroomDTO.getUsers());
        allUserSeqs.add(myUserSeq);

        // 1:1 채팅방 생성일 경우 중복 확인
        if(allUserSeqs.size() == 2) {
            List<Long> existingChatroomSeq = chatRepository.findExistingChatroom(allUserSeqs);
            if(!existingChatroomSeq.isEmpty()) {
                log.info("기존 1:1 채팅방이 존재합니다. chatroomSeq: {}", existingChatroomSeq.get(0));
                return existingChatroomSeq.get(0);
            }
            log.info("기존 1:1 채팅방이 존재하지 않습니다.");
        }

        Chatroom chatroom = Chatroom.builder()
                .chatroomDefaultName("채팅방")
                .build();
        Chatroom savedChatroom = chatroomRepository.save(chatroom); // 채팅방 저장 후 생성된 chatGroupSeq를 반환
        Long chatroomSeq = savedChatroom.getChatroomSeq();

        // 사용자 리스트 한번에 가져오기
        List<User> users = userService.findUsersBySeqs(allUserSeqs);

        // Chat 테이블에 사용자 정보 추가, 채팅방 기본 이름 설정
        StringBuilder usersInChatGroup = new StringBuilder();
        for (User user : users) {

            usersInChatGroup.append(user.getUserName()).append(" ");    // 채팅방 참여자 이름 나열

            Chat chat = Chat.builder()
                    .user(user)
                    .chatroom(savedChatroom)
                    .lastVisitedAt(LocalDateTime.now())
                    .build();

            chatRepository.save(chat);   // 채팅방 참여자를 Chat 테이블에 추가
        }

        String chatroomDefaultName = usersInChatGroup.toString().trim();
        savedChatroom.updateChatroomDefaultName(chatroomDefaultName);

        notifyServiceImlp.sendChat(users);

        sendNotifyMessage(chatroomSeq, myUserSeq, chatroomDTO.getUsers(), "create");

        return chatroomSeq;
    }

    /* 대화상대 초대(추가) */
    @Transactional
    public void updateChatroomMember(Long myUserSeq, Long chatroomSeq, ChatroomDTO chatroomDTO) {
        Chatroom chatroom = chatroomRepository.findByChatroomSeq(chatroomSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));

        StringBuilder usersInChatGroup = new StringBuilder();
        usersInChatGroup.append(chatroom.getChatroomDefaultName()).append(" ");

        // 사용자 리스트 한번에 가져오기
        List<Long> allUserSeqs = new ArrayList<>(chatroomDTO.getUsers());
        List<User> users = userService.findUsersBySeqs(allUserSeqs);

        for (User user : users) {
            // 이미 채팅방에 참여 중인지 확인
            if (chatRepository.existsByChatroomChatroomSeqAndUserUserSeq(chatroomSeq, user.getUserSeq())) {
                throw new CustomException(ErrorCode.USER_ALREADY_IN_CHATROOM);
            }

            usersInChatGroup.append(user.getUserName()).append(" ");

            Chat chat = Chat.builder()
                    .user(user)
                    .chatroom(chatroom)
                    .lastVisitedAt(LocalDateTime.now())
                    .build();

            chatRepository.save(chat);
        }

        String chatroomDefaultName = usersInChatGroup.toString().trim();
        chatroom.updateChatroomDefaultName(chatroomDefaultName);

        notifyServiceImlp.sendChat(users);

        sendNotifyMessage(chatroomSeq, myUserSeq, chatroomDTO.getUsers(), "invite");
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

        sendNotifyMessage(chatroomSeq, userSeq, null, "leave");
    }

    /* 사용자별 채팅 목록 조회 */
    @Transactional(readOnly = true)
    public List<ResponseChatroomDTO> getChatroomListByUserSeq(Long userSeq) {
        // 사용자가 참여 중인 채팅방 목록 조회
        List<Chat> chats = chatRepository.findByUser_UserSeq(userSeq);
        log.info("사용자 {}의 채팅방 조회 - {}개 채팅방 반환", userSeq, chats.size());

        // 각 채팅방에 대한 정보를 DTO로 매핑
        return chats.stream()
                .map(chat -> {
                    // 채팅방 정보 가져오기
                    Chatroom chatroom = chat.getChatroom();
                    Long chatroomSeq = chat.getChatroom().getChatroomSeq();

                    // 해당 채팅방에 참여한 사용자 수
                    Long chatroomUsersCount = chatRepository.countByChatroom_ChatroomSeq(chatroom.getChatroomSeq());

                    // 마지막 메시지 조회 (최근 메시지 1개만 가져오기)
                    ChatMessage lastMessage = chatMessageRepository.findTopByChatroomSeqAndIsDeletedFalseOrderByCreatedAtDesc(chatroom.getChatroomSeq());
                    String lastMessageText = (lastMessage != null) ? lastMessage.getMessage() : "No messages yet";
                    LocalDateTime lastMessageTime = (lastMessage != null) ? lastMessage.getCreatedAt().minusHours(9) : null;

                    // 읽지 않은 메시지 개수 계산
                    Long unreadMessageCount = chatMessageRepository.countByChatroomSeqAndCreatedAtAfterAndIsDeletedFalse(chatroomSeq, chat.getLastVisitedAt().plusHours(9));

                    // ResponseChatroomDTO로 변환
                    return ResponseChatroomDTO.builder()
                            .chatroomSeq(chatroom.getChatroomSeq())                 // 채팅방 Seq
                            .chatroomDefaultName(chatroom.getChatroomDefaultName()) // 채팅방 기본 이름
                            .chatroomCustomName(chat.getChatroomCustomName())       // 사용자별 채팅방 설정 이름
                            .chatroomUsersCount(chatroomUsersCount)                 // 채팅방 사용자 수
                            .lastMessage(lastMessageText)                           // 마지막 메시지
                            .lastMessageTime(lastMessageTime)
                            .unreadMessageCount(unreadMessageCount)
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
        Long unreadMessageCount = chatMessageRepository.countByChatroomSeqAndCreatedAtAfterAndIsDeletedFalse(chatroomSeq, chat.getLastVisitedAt());

        return ResponseChatroomDTO.builder()
                .chatroomSeq(chatroom.getChatroomSeq())
                .chatroomDefaultName(chatroom.getChatroomDefaultName())
                .chatroomCustomName(chat.getChatroomCustomName())
                .chatroomUsersCount(chatroomUsersCount)
                .unreadMessageCount(unreadMessageCount)
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

    /* 채팅방 입장, 퇴장 안내 메시지 */
    @Transactional
    public void sendNotifyMessage(Long chatroomSeq, Long myUserSeq, List<Long> targetUserSeqs, String action) {
        // 1. 요청 사용자 정보 조회
        User sender = userService.findUser(myUserSeq);

        // 2. 대상 사용자 이름 가져오기
        String targetUserNames = "";
        if(targetUserSeqs != null && !targetUserSeqs.isEmpty()) {
            List<User> targetUsers = userService.findUsersBySeqs(targetUserSeqs);
            targetUserNames = targetUsers.stream().map(User::getUserName).collect(Collectors.joining(", "));
        }

        // 3. 알림 메시지 내용 생성
        String messageContent;
        if(action.equals("create") || action.equals("invite")) {
            messageContent = String.format("'%s'님이 '%s'님을 초대하셨습니다.", sender.getUserName(), targetUserNames);
        } else if(action.equals("leave")) {
            messageContent = String.format("'%s'님이 채팅방에서 나가셨습니다.", sender.getUserName());
        } else {
            throw new IllegalArgumentException("알 수 없는 action 값: " + action);
        }

        // 4. ChatMessage 생성 및 MongoDB 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .chatroomSeq(chatroomSeq)
                .senderUserSeq(null)
                .type("notify")
                .message(messageContent)
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        // 5. ResponseChatMessageDTO 생성
        ResponseChatMessageDTO responseChatMessageDTO = ResponseChatMessageDTO.builder()
                .messageSeq(savedMessage.getId())
                .chatroomSeq(chatroomSeq)
                .senderUserSeq(null)
                .senderUserName("System")
                .senderUserProfileUrl(null)
                .type("notify")
                .message(messageContent)
                .createdAt(savedMessage.getCreatedAt().minusHours(9))
                .build();

        // 6. Kafka로 메시지 전송
        kafkaProducerService.sendMessageToKafka(responseChatMessageDTO);

        log.info("Kafka notify 메시지 전송 완료: {}", responseChatMessageDTO);
    }

    /* 채팅방 마지막 방문 시간 업데이트 */
    @Transactional
    public void updateLastVisitedTime(Long userSeq, Long chatroomSeq) {
        Chat chat = chatRepository.findByUser_UserSeqAndChatroom_ChatroomSeq(userSeq, chatroomSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));

        log.info("Before update: {}", chat.getLastVisitedAt());
        chat.updateLastVisitedAt(LocalDateTime.now());
        log.info("After update: {}", chat.getLastVisitedAt());
        chatRepository.save(chat);
    }
}
