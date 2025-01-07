package mediHub_be.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.service.PictureService;
import mediHub_be.chat.dto.ChatMessageDTO;
import mediHub_be.chat.dto.ResponseAttachFileDTO;
import mediHub_be.chat.dto.ResponseChatMessageDTO;
import mediHub_be.chat.entity.ChatMessage;
import mediHub_be.chat.repository.ChatMessageRepository;
import mediHub_be.chat.repository.ChatRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.common.utils.TimeFormatUtil;
import mediHub_be.config.amazonS3.AmazonS3Service;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import mediHub_be.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PictureService pictureService;
    private final AmazonS3Service amazonS3Service;
    private final KafkaProducerService kafkaProducerService;

    /* 채팅 메시지 저장 */
    public ResponseChatMessageDTO saveMessage(ChatMessageDTO message) {

        ChatMessage newMessage = ChatMessage.builder()
                .chatroomSeq(message.getChatroomSeq())
                .senderUserSeq(message.getSenderUserSeq())
                .type(message.getType())
                .message(message.getMessage())
                .createdAt(message.getCreatedAt())
                .build();
        chatMessageRepository.save(newMessage);

        // ChatMessage -> ResponseChatMessageDTO 변환
        User senderUser = userService.findUser(message.getSenderUserSeq());
        String profileUrl = pictureService.getUserProfileUrl(senderUser.getUserSeq());

        return ResponseChatMessageDTO.builder()
                .messageSeq(newMessage.getId())
                .chatroomSeq(newMessage.getChatroomSeq())
                .senderUserSeq(newMessage.getSenderUserSeq())
                .senderUserName(senderUser.getUserName())
                .senderUserProfileUrl(profileUrl)
                .type(newMessage.getType())
                .message(newMessage.getMessage())
                .createdAt(newMessage.getCreatedAt())
                .build();
    }

    /* 첨부파일 메시지 저장 */
    public ResponseChatMessageDTO saveFileMessage(ChatMessageDTO message, MultipartFile file) {

        try {
            // S3에 파일 업르도 및 메타데이터 반환
            AmazonS3Service.MetaData metaData = amazonS3Service.upload(file);

            // 파일 이름에서 확장자 추출
            String originalFileName = file.getOriginalFilename();
            String extension = getFileExtension(originalFileName);
            String type = isImageExtension(extension) ? "image" : "file";

            // 첨부파일 객체 생성
            ChatMessage.Attachment attachment = ChatMessage.Attachment.builder()
                    .originName(metaData.getOriginalFileName())
                    .url(metaData.getUrl())
                    .build();

            // Message 생성 및 저장
            ChatMessage newMessage = ChatMessage.builder()
                    .chatroomSeq(message.getChatroomSeq())
                    .senderUserSeq(message.getSenderUserSeq())
                    .type(type)
                    .message(message.getMessage())
                    .createdAt(message.getCreatedAt())
                    .attachment(attachment)
                    .build();
            chatMessageRepository.save(newMessage);

            // ChatMessage -> ResponseChatMessageDTO 변환
            User senderUser = userService.findUser(message.getSenderUserSeq());
            String profileUrl = pictureService.getUserProfileUrl(senderUser.getUserSeq());

            return ResponseChatMessageDTO.builder()
                    .messageSeq(newMessage.getId())
                    .chatroomSeq(newMessage.getChatroomSeq())
                    .senderUserSeq(newMessage.getSenderUserSeq())
                    .senderUserName(senderUser.getUserName())
                    .senderUserProfileUrl(profileUrl)
                    .type(newMessage.getType())
                    .message(newMessage.getMessage())
                    .createdAt(newMessage.getCreatedAt())
                    .attachment(newMessage.getAttachment())
                    .build();
        } catch(IOException e) {
            log.error("[채팅] 첨부파일 저장 중 오류 발생", e);
            throw new RuntimeException("첨부파일 저장 실패");
        }
    }

    // 파일 이름에서 확장자 추출
    private String getFileExtension(String fileName) {
        if(fileName == null || !fileName.contains(".")) {
            return "";  // 확장자가 없는 경우 빈 문자열 반환
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    // 이미지 확장자인지 확인
    private boolean isImageExtension(String extension) {
        // 이미지 확장자 목록 정의
        List<String> imageExtensions = Arrays.asList("png", "jpg", "jpeg", "gif", "bmp", "webp");
        return imageExtensions.contains(extension);
    }

    /* 채팅 메시지 삭제 */
    @Transactional
    public void deleteMessage(String messageId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATMESSAGE));

        // 논리 삭제
        message.setIsDeleted(true);
        chatMessageRepository.save(message);

        // Kafka로 삭제 이벤트 전송
        kafkaProducerService.sendDeleteMessage("chat-topic", messageId, message.getChatroomSeq());
    }

    /* 특정 채팅방의 메시지 조회 */
    public List<ResponseChatMessageDTO> getMessagesByRoomSeq(Long userSeq, Long chatroomSeq) {
        // 사용자의 채팅방 참여 날짜와 시간을 조회
        LocalDateTime userJoinDateTime = chatRepository.findJoinDateByUserSeqAndChatroomSeq(userSeq, chatroomSeq);
        log.info("사용자 {}의 채팅방 {} 참여 날짜 : {}", userSeq, chatroomSeq, userJoinDateTime);

        // 참여 시간 이후의 메시지만 조회
        List<ChatMessage> messages = chatMessageRepository.findByChatroomSeqAndCreatedAtAfterAndIsDeletedFalseOrderByCreatedAtAsc(chatroomSeq, userJoinDateTime.plusHours(9));
        log.info("채팅방 {}의 메세지 조회 - {}개 메시지 반환", chatroomSeq, messages.size());

        // 모든 senderUserSeq를 추출하여 사용자 정보 한 번에 조회
        List<Long> userSeqs = messages.stream()
                .map(ChatMessage::getSenderUserSeq)
                .filter(seq -> seq != null) // null 값 제거
                .distinct()  // 중복 제거
                .collect(Collectors.toList());

        List<User> users = userRepository.findByUserSeqIn(userSeqs);

        // 사용자 정보를 Map으로 변환
        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getUserSeq, user -> user));

        // 메시지를 변환할 때 사용자 정보를 조회
        return messages.stream()
                .map(message -> {
                    String profileUrl = null; // 기본적으로 null 설정
                    User senderUser = null;

                    if(message.getSenderUserSeq() != null) {
                        senderUser = userMap.get(message.getSenderUserSeq()); // 미리 조회한 사용자 정보 사용
                        if(senderUser != null) {
                            profileUrl = pictureService.getUserProfileUrl(senderUser.getUserSeq());
                        }
                    }

                    return ResponseChatMessageDTO.builder()
                            .messageSeq(message.getId())
                            .chatroomSeq(message.getChatroomSeq())
                            .senderUserSeq(message.getSenderUserSeq())
                            .senderUserName(senderUser != null ? senderUser.getUserName() : "Unknown") // 사용자 이름 추가
                            .senderUserProfileUrl(profileUrl != null ? profileUrl : "System")
                            .type(message.getType())
                            .message(message.getMessage())
                            .createdAt(message.getCreatedAt())  // 시간대 조정
                            .attachment(message.getAttachment())
                            .build();
                })
                .collect(Collectors.toList());

    }

    /* 사용자별 첨부파일 목록 조회 */
    public List<ResponseAttachFileDTO> getFilesByUserSeq(Long userSeq) {
        // 사용자가 참여 중인 모든 채팅방 chatroomSeq, joinedAt 조회
        List<ChatRepository.ChatroomJoinInfo> joinInfos = chatRepository.findJoinInfoByUserSeq(userSeq);

        // isDeleted = false && createdAt > joinedAt 인 첨부파일 메시지 조회
        List<ChatMessage> messagesWithAttachments = joinInfos.stream()
                .flatMap(joinInfo ->
                        chatMessageRepository.findByChatroomSeqAndCreatedAtAfterAndIsDeletedFalseAndAttachmentIsNotNull
                                (joinInfo.getChatroomSeq(), joinInfo.getJoinedAt()).stream())
                .collect(Collectors.toList());

        // ChatMessage -> ResponseAttachFileDTO 반환
        return messagesWithAttachments.stream()
                .map(message -> {
                    User senderUser = userService.findUser(message.getSenderUserSeq());

                    return ResponseAttachFileDTO.builder()
                            .messageSeq(message.getId())
                            .senderUserSeq(senderUser.getUserSeq())
                            .senderUserName(senderUser.getUserName())
                            .rankingName(senderUser.getRanking().getRankingName())
                            .type(message.getType())
                            .createdAt(TimeFormatUtil.yearAndMonthDay(message.getCreatedAt()).format(TimeFormatUtil.yMDFormatter))
                            .attachment(message.getAttachment())
                            .build();
                }).collect(Collectors.toList());
    }
}
