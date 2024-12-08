package mediHub_be.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotResponseDTO {
    private String id;
    private String object;
    private Long created;
    private List<ChoiceDTO> choices;

}
