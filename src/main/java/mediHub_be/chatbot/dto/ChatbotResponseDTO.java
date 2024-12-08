package mediHub_be.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotResponseDTO {
    private String id;                  // 응답 ID
    private String object;              // 응답 객체 타입
    private Long created;               // 응답 생성 시간
    private List<ChoiceDTO> choices;    // 응답 선택지 리스트
}
