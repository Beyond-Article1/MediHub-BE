package mediHub_be.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotRequestDTO {
    private String model;
    private List<MessageDTO> messages;


}
