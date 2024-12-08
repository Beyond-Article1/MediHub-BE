package mediHub_be.chatbot.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceDTO {
    private Integer index;              // 선택지 인덱스
    private MessageDTO message;         // 선택지 메시지
    private String finishReason;        // 응답 완료 사유 (e.g., "stop")
}

