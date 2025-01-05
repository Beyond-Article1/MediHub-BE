package mediHub_be.chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import mediHub_be.config.ChatbotPromptConfig;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Getter
@Component
public class ChatbotPromptLoader {
    private final ChatbotPromptConfig promptConfig;

    public ChatbotPromptLoader() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        promptConfig = objectMapper.readValue(
                new File("src/main/resources/chatbot-prompts.json"),
                ChatbotPromptConfig.class
        );
    }

}
