package mediHub_be.chatbot.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(collection = "chatbot_messages")
public class ChatbotMessage {

    @Id
    private String id; // MongoDB ObjectId

    private final String sessionId; // 세션 ID
    private final String sender; // 발신자 (user/bot)
    private final String content; // 메시지 내용
    private final LocalDateTime timestamp; // 메시지 전송 시간

    @Builder
    public ChatbotMessage(String sessionId, String sender, String content, LocalDateTime timestamp) {
        this.sessionId = sessionId;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

}
