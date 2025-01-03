package mediHub_be.chatbot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chatbot_sessions")
public class ChatbotSession {

    @Id
    private String id; // MongoDB ObjectId

    private Long userSeq; // 사용자 Seq
    private String title; // 세션 제목

    private LocalDateTime createdAt; // 세션 생성 시간

    private LocalDateTime lastMessageAt; // 마지막 메시지 시간
    private String lastMessageContent; // 마지막 메시지 내용

    @Builder
    public ChatbotSession(Long userSeq, String title, LocalDateTime createdAt) {
        this.userSeq = userSeq;
        this.title = title;
        this.createdAt = createdAt;
    }

    public void updateLastMessage(LocalDateTime lastMessageAt, String lastMessageContent) {
        this.lastMessageAt = lastMessageAt;
        this.lastMessageContent = lastMessageContent;
    }

    public void updateTitle(String newTitle) {
        this.title = newTitle;
    }
}
