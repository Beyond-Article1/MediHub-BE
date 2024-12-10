package mediHub_be.chat.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.chat.dto.ChatMessageDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    public ChatMessageDTO saveMessage(ChatMessageDTO message) {
        return null;
    }

    public List<ChatMessageDTO> getMessagesByRoomSeq(String chatGroupSeq) {
        return null;
    }

}
