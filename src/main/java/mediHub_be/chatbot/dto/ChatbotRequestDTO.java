package mediHub_be.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotRequestDTO {
    private String model; // 사용할 모델 (예: gpt-3.5-turbo)
    private List<MessageDTO> messages; // 메시지 리스트
}
