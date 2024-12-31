package mediHub_be.chat.service;

import mediHub_be.chat.dto.ChatroomDTO;
import mediHub_be.chat.dto.ResponseChatMessageDTO;
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
import mediHub_be.notify.service.NotifyServiceImlp;
import mediHub_be.user.entity.User;
import mediHub_be.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChatroomServiceTest {

    @InjectMocks
    private ChatroomService chatroomService;

    @Mock
    private ChatroomRepository chatroomRepository;

    @Mock
    ChatRepository chatRepository;

    @Mock
    ChatMessageRepository chatMessageRepository;

    @Mock
    private UserService userService;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @Mock
    private NotifyServiceImlp notifyServiceImlp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("채팅방 생성 성공 - 새로운 채팅방")
    @Test
    void testCreateChatroom_Success() {
        // Given
        Long myUserSeq = 1L;
        ChatroomDTO chatroomDTO = new ChatroomDTO();
        chatroomDTO.setUsers(List.of(2L));  // 초대할 사용자 UserSeq

        List<Long> allUserSeqs = new ArrayList<>(chatroomDTO.getUsers());
        allUserSeqs.add(myUserSeq);

        // Mock 사용자 객체 생성
        User mockUser1 = mock(User.class);
        when(mockUser1.getUserSeq()).thenReturn(1L);
        when(mockUser1.getUserName()).thenReturn("동그리");

        User mockUser2 = mock(User.class);
        when(mockUser2.getUserSeq()).thenReturn(2L);
        when(mockUser2.getUserName()).thenReturn("상혀니");

        List<User> users = List.of(mockUser1, mockUser2);

        // Mock 채팅방 객체 생성
        Chatroom mockChatroom = mock(Chatroom.class);
        when(mockChatroom.getChatroomSeq()).thenReturn(100L);

        // Mock 메시지 객체 생성
        ChatMessage mockChatMessage = mock(ChatMessage.class);
        when(mockChatMessage.getId()).thenReturn("mock-message-id");
        when(mockChatMessage.getCreatedAt()).thenReturn(LocalDateTime.now());

        // Mock 동작 정의
        when(chatRepository.findExistingChatroom(allUserSeqs)).thenReturn(new ArrayList<>());
        when(userService.findUsersBySeqs(allUserSeqs)).thenReturn(users);
        when(userService.findUser(myUserSeq)).thenReturn(mockUser1);
        when(chatroomRepository.save(any(Chatroom.class))).thenReturn(mockChatroom);
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(mockChatMessage);

        // When
        Long chatroomSeq = chatroomService.createChatroom(myUserSeq, chatroomDTO);

        // Then
        assertNotNull(chatroomSeq);
        assertEquals(100L, chatroomSeq);

        // Verify 호출 확인
        verify(chatRepository, times(1)).findExistingChatroom(allUserSeqs);
        verify(userService, times(1)).findUsersBySeqs(allUserSeqs);
        verify(userService, times(1)).findUser(myUserSeq);
        verify(chatroomRepository, times(1)).save(any(Chatroom.class));
        verify(notifyServiceImlp, times(1)).sendChat(users);
        verify(chatMessageRepository, times(1)).save(any(ChatMessage.class));
        verify(kafkaProducerService, times(1)).sendMessageToKafka(any(ResponseChatMessageDTO.class));
    }

    @DisplayName("채팅방 생성 - 기존 1:1 채팅방 존재")
    @Test
    void testCreateChatroom_ExistingOneToOneChatroom() {
        // Given
        Long myUserSeq = 1L;
        ChatroomDTO chatroomDTO = new ChatroomDTO();
        chatroomDTO.setUsers(List.of(2L));

        List<Long> allUserSeqs = new ArrayList<>(chatroomDTO.getUsers());
        allUserSeqs.add(myUserSeq);

        // Mock 동작 정의: 기존 1:1 채팅방이 존재하는 경우
        when(chatRepository.findExistingChatroom(allUserSeqs)).thenReturn(List.of(50L));

        // When
        Long chatroomSeq = chatroomService.createChatroom(myUserSeq, chatroomDTO);

        // Then
        assertNotNull(chatroomSeq);
        assertEquals(50L, chatroomSeq);

        // Verify 호출 확인 (기존 채팅방은 저장되지 않음)
        verify(chatRepository, times(1)).findExistingChatroom(allUserSeqs);
    }

    @DisplayName("대화 상대 초대 성공")
    @Test
    void testUpdateChatroomMember_Success() {
        // Given
        Long myUserSeq = 1L;        // 요청 사용자 Seq
        Long chatroomSeq = 100L;

        ChatroomDTO chatroomDTO = new ChatroomDTO();
        chatroomDTO.setUsers(List.of(2L, 3L));

        // Mock 채팅방 객체 생성
        Chatroom mockChatroom = mock(Chatroom.class);
        when(mockChatroom.getChatroomDefaultName()).thenReturn("동그리");
        when(chatroomRepository.findByChatroomSeq(chatroomSeq)).thenReturn(java.util.Optional.of(mockChatroom));

        // Mock 사용자 객체 생성
        User mockUser1 = mock(User.class);
        when(mockUser1.getUserSeq()).thenReturn(1L);
        when(mockUser1.getUserName()).thenReturn("동그리");

        User mockUser2 = mock(User.class);
        when(mockUser2.getUserSeq()).thenReturn(2L);
        when(mockUser2.getUserName()).thenReturn("상혀니");

        User mockUser3 = mock(User.class);
        when(mockUser3.getUserSeq()).thenReturn(3L);
        when(mockUser3.getUserName()).thenReturn("채류니");

        List<User> users = List.of(mockUser2, mockUser3);

        // Mock 메시지 객체 생성
        ChatMessage mockChatMessage = mock(ChatMessage.class);
        when(mockChatMessage.getId()).thenReturn("mock-message-id");
        when(mockChatMessage.getCreatedAt()).thenReturn(LocalDateTime.now());

        // Mock 동작 정의
        when(userService.findUsersBySeqs(chatroomDTO.getUsers())).thenReturn(users);
        when(userService.findUser(myUserSeq)).thenReturn(mockUser1);
        when(chatRepository.existsByChatroomChatroomSeqAndUserUserSeq(chatroomSeq, 2L)).thenReturn(false);
        when(chatRepository.existsByChatroomChatroomSeqAndUserUserSeq(chatroomSeq, 3L)).thenReturn(false);
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(mockChatMessage);

        // When
        chatroomService.updateChatroomMember(myUserSeq, chatroomSeq, chatroomDTO);

        // Then
        verify(userService, times(1)).findUser(myUserSeq);
        verify(chatRepository, times(1)).existsByChatroomChatroomSeqAndUserUserSeq(chatroomSeq, 2L);
        verify(chatRepository, times(1)).existsByChatroomChatroomSeqAndUserUserSeq(chatroomSeq, 3L);
        verify(chatRepository, times(2)).save(any(Chat.class)); // 초대된 사용자 2명 저장 확인
        verify(notifyServiceImlp, times(1)).sendChat(users);
    }

    @DisplayName("대화 상대 초대 실패 - 이미 참여 중인 사용자")
    @Test
    void testUpdateChatroomMember_UserAlreadyInChatroom() {
        // Given
        Long myUserSeq = 1L;
        Long chatroomSeq = 100L;

        ChatroomDTO chatroomDTO = new ChatroomDTO();
        chatroomDTO.setUsers(List.of(2L));

        // Mock 채팅방 객체 생성
        Chatroom mockChatRoom = mock(Chatroom.class);
        when(chatroomRepository.findByChatroomSeq(chatroomSeq))
                .thenReturn(java.util.Optional.of(mockChatRoom));

        // Mock 사용자 객체 생성
        User mockUser2 = mock(User.class);
        when(mockUser2.getUserSeq()).thenReturn(2L);
        when(mockUser2.getUserName()).thenReturn("상혀니");

        // Mock 동작 정의: 이미 채팅방에 참여 중인 사용자
        when(userService.findUsersBySeqs(chatroomDTO.getUsers())).thenReturn(List.of(mockUser2));
        when(chatRepository.existsByChatroomChatroomSeqAndUserUserSeq(chatroomSeq, 2L)).thenReturn(true);

        // When & Then
        CustomException exception = assertThrows(CustomException.class,
                () -> chatroomService.updateChatroomMember(myUserSeq, chatroomSeq, chatroomDTO));

        // 예외 메시지 및 코드 확인
        assertEquals(ErrorCode.USER_ALREADY_IN_CHATROOM, exception.getErrorCode());

        // Verify 호출 확인
        verify(chatRepository, times(1)).existsByChatroomChatroomSeqAndUserUserSeq(chatroomSeq, 2L);
        verify(chatRepository, never()).save(any()); // 저장 로직이 호출되지 않아야 함
    }

    @DisplayName("채팅방 이름 변경 성공")
    @Test
    void testUpdateChatroomName_Success() {
        // Given
        Long userSeq = 1L;
        Long chatroomSeq = 100L;
        UpdateChatroomDTO updateChatroomDTO = new UpdateChatroomDTO();
        updateChatroomDTO.setChatroomCustomName("제1조1항 수다방");

        // Mock 채팅 객체 생성
        Chat mockChat = mock(Chat.class);
        when(chatRepository.findByUser_UserSeqAndChatroom_ChatroomSeq(userSeq, chatroomSeq)).thenReturn(Optional.of(mockChat));

        // When
        chatroomService.updateChatroomName(userSeq, chatroomSeq, updateChatroomDTO);

        // Then
        verify(mockChat, times(1)).updateChatroomName("제1조1항 수다방");
        verify(chatRepository, times(1)).save(mockChat);
    }

    @DisplayName("채팅방 이름 변경 실패 - 채팅방이 존재하지 않음")
    @Test
    void testUpdateChatroomName_ChatroomNotFound() {
        // Given
        Long userSeq = 1L;
        Long chatroomSeq = 100L;
        UpdateChatroomDTO updateChatroomDTO = new UpdateChatroomDTO();
        updateChatroomDTO.setChatroomCustomName("바보들의 점메추");

        when(chatRepository.findByUser_UserSeqAndChatroom_ChatroomSeq(userSeq, chatroomSeq)).thenReturn(Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class,
                () -> chatroomService.updateChatroomName(userSeq, chatroomSeq, updateChatroomDTO));
        assertEquals(ErrorCode.NOT_FOUND_CHATROOM, exception.getErrorCode());
    }

    @DisplayName("채팅방 나가기 성공")
    @Test
    void testDeleteChatroom_Success() {
        // Given
        Long userSeq = 1L;
        Long chatroomSeq = 100L;

        // Mock 채팅 객체 생성
        Chat mockChat = mock(Chat.class);
        when(chatRepository.findByUser_UserSeqAndChatroom_ChatroomSeq(userSeq, chatroomSeq)).thenReturn(Optional.of(mockChat));

        // Mock 채팅방에 남아있는 사용자
        List<Chat> remainingChats = new ArrayList<>();
        Chat remainingChat1 = mock(Chat.class);
        Chat remainingChat2 = mock(Chat.class);

        // Mock 사용자 객체 생성
        User mockUser1 = mock(User.class);
        when(mockUser1.getUserSeq()).thenReturn(1L);
        when(mockUser1.getUserName()).thenReturn("동그리");
        when(userService.findUser(userSeq)).thenReturn(mockUser1);

        User mockUser2 = mock(User.class);
        when(mockUser2.getUserName()).thenReturn("상혀니");

        User mockUser3 = mock(User.class);
        when(mockUser3.getUserName()).thenReturn("채류니");

        // Mock remainingChats의 getUser() 동작 정의
        when(remainingChat1.getUser()).thenReturn(mockUser2);
        when(remainingChat2.getUser()).thenReturn(mockUser3);
        remainingChats.add(remainingChat1);
        remainingChats.add(remainingChat2);

        when(chatRepository.countByChatroom_ChatroomSeq(chatroomSeq)).thenReturn(2L);   // 남아있는 사용자 수
        when(chatRepository.findByChatroom_ChatroomSeq(chatroomSeq)).thenReturn(remainingChats);

        // Mock 채팅방 객체 생성
        Chatroom mockChatroom = mock(Chatroom.class);
        when(chatroomRepository.findById(chatroomSeq)).thenReturn(Optional.of(mockChatroom));

        // Mock 메시지 객체 생성
        ChatMessage mockChatMessage = mock(ChatMessage.class);
        when(mockChatMessage.getId()).thenReturn("mock-message-id");
        when(mockChatMessage.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(mockChatMessage);

        // When
        chatroomService.deleteChatroom(userSeq, chatroomSeq);

        // Then
        verify(chatRepository, times(1)).delete(mockChat); // 나가는 사용자 삭제 확인
        verify(chatRepository, times(1)).countByChatroom_ChatroomSeq(chatroomSeq); // 남은 사용자 수 확인
        verify(chatRepository, times(1)).findByChatroom_ChatroomSeq(chatroomSeq); // 남은 사용자 목록 조회 확인
        verify(mockChatroom, times(1)).updateChatroomDefaultName("상혀니 채류니"); // 채팅방 이름 업데이트 확인
        verify(chatroomRepository, times(1)).save(mockChatroom); // 채팅방 저장 확인
    }

    @DisplayName("사용자별 채팅 목록 조회 성공")
    @Test
    void testGetChatroomListByUserSeq_Success() {
        // Given
        Long userSeq = 1L;

        // Mock 채팅방 및 사용자 데이터 생성
        Chatroom mockChatroom1 = mock(Chatroom.class);
        when(mockChatroom1.getChatroomSeq()).thenReturn(100L);
        when(mockChatroom1.getChatroomDefaultName()).thenReturn("채팅방 1");

        Chat mockChat1 = mock(Chat.class);
        when(mockChat1.getChatroom()).thenReturn(mockChatroom1);
        when(mockChat1.getLastVisitedAt()).thenReturn(LocalDateTime.now().minusDays(1));
        when(mockChat1.getChatroomCustomName()).thenReturn("동그리 채팅방 1");

        Chatroom mockChatroom2 = mock(Chatroom.class);
        when(mockChatroom2.getChatroomSeq()).thenReturn(200L);
        when(mockChatroom2.getChatroomDefaultName()).thenReturn("채팅방 2");

        Chat mockChat2 = mock(Chat.class);
        when(mockChat2.getChatroom()).thenReturn(mockChatroom2);
        when(mockChat2.getLastVisitedAt()).thenReturn(LocalDateTime.now().minusDays(2));
        when(mockChat2.getChatroomCustomName()).thenReturn("동그리 채팅방 2");

        List<Chat> chats = List.of(mockChat1, mockChat2);

        // Mock 메시지 데이터 생성
        ChatMessage mockLastMessage1 = mock(ChatMessage.class);
        when(mockLastMessage1.getMessage()).thenReturn("마지막 메시지 1");
        when(mockLastMessage1.getCreatedAt()).thenReturn(LocalDateTime.now().minusHours(9));

        ChatMessage mockLastMessage2 = mock(ChatMessage.class);
        when(mockLastMessage2.getMessage()).thenReturn("마지막 메시지 2");
        when(mockLastMessage2.getCreatedAt()).thenReturn(LocalDateTime.now().minusHours(9));

        // Mock 동작 정의
        when(chatRepository.findByUser_UserSeq(userSeq)).thenReturn(chats);
        when(chatRepository.countByChatroom_ChatroomSeq(100L)).thenReturn(3L); // 채팅방 1 사용자 수
        when(chatRepository.countByChatroom_ChatroomSeq(200L)).thenReturn(5L); // 채팅방 2 사용자 수
        when(chatMessageRepository.findTopByChatroomSeqAndIsDeletedFalseOrderByCreatedAtDesc(anyLong()))
                .thenAnswer(invocation -> {
                    Long chatroomSeq = invocation.getArgument(0);
                    return chatroomSeq == 100L ? mockLastMessage1 : mockLastMessage2; // 채팅방 Seq에 따라 메시지 반환
                });
        when(chatMessageRepository.countByChatroomSeqAndCreatedAtAfterAndIsDeletedFalse(anyLong(), any()))
                .thenAnswer(invocation -> {
                    Long chatroomSeq = invocation.getArgument(0);
                    return chatroomSeq == 100L ? 10L : 20L; // 채팅방 Seq에 따라 읽지 않은 메시지 개수 반환
                });

        // When
        List<ResponseChatroomDTO> result = chatroomService.getChatroomListByUserSeq(userSeq);

        // Then
        assertEquals(2, result.size()); // 두 개의 채팅방 반환 확인

        ResponseChatroomDTO chatRoom1 = result.get(0);
        assertEquals(100L, chatRoom1.getChatroomSeq());
        assertEquals("채팅방 1", chatRoom1.getChatroomDefaultName());
        assertEquals("동그리 채팅방 1", chatRoom1.getChatroomCustomName());
        assertEquals(3L, chatRoom1.getChatroomUsersCount());
        assertEquals("마지막 메시지 1", chatRoom1.getLastMessage());
        assertEquals(10L, chatRoom1.getUnreadMessageCount());

        ResponseChatroomDTO chatRoom2 = result.get(1);
        assertEquals(200L, chatRoom2.getChatroomSeq());
        assertEquals("채팅방 2", chatRoom2.getChatroomDefaultName());
        assertEquals("동그리 채팅방 2", chatRoom2.getChatroomCustomName());
        assertEquals(5L, chatRoom2.getChatroomUsersCount());
        assertEquals("마지막 메시지 2", chatRoom2.getLastMessage());
        assertEquals(20L, chatRoom2.getUnreadMessageCount());

        // Verify 호출 확인
        verify(chatRepository, times(1)).findByUser_UserSeq(userSeq);
    }

}
