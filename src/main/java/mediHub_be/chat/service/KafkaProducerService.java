package mediHub_be.chat.service;

import mediHub_be.chat.dto.ResponseChatMessageDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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
}
