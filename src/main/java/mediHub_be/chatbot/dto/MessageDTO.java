package mediHub_be.chatbot.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageDTO {
    private String role; // user or ai
    private String content;

    @JsonCreator
    public MessageDTO(@JsonProperty("sender") String sender, @JsonProperty("content") String content) {
        this.role = sender;
        this.content = content;
    }
}