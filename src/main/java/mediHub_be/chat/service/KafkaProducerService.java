package mediHub_be.chat.service;

import lombok.extern.slf4j.Slf4j;
import mediHub_be.chat.dto.ResponseChatMessageDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, ResponseChatMessageDTO> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, ResponseChatMessageDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessageToKafka(ResponseChatMessageDTO message) {
        String topic = "chat-topic";  // 채팅방별 토픽
        kafkaTemplate.send(topic, message);  // Kafka에 메시지 전송
    }

    public void sendDeleteMessage(String topic, String messageSeq, Long chatroomSeq) {
        // 삭제 이벤트용 ResponseChatMessageDTO 생성
        ResponseChatMessageDTO deleteEvent = ResponseChatMessageDTO.builder()
                .messageSeq(messageSeq)
                .chatroomSeq(chatroomSeq)
                .type("delete")
                .build();

        kafkaTemplate.send(topic, deleteEvent);
        log.info("Kafka로 삭제 메시지 전송: {}", deleteEvent);
    }
}
