package mediHub_be.chat.service;

import mediHub_be.chat.dto.ChatMessageDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, ChatMessageDTO> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, ChatMessageDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessageToKafka(ChatMessageDTO message) {
        String topic = "chat-topic";  // 채팅방별 토픽
        kafkaTemplate.send(topic, message);  // Kafka에 메시지 전송
    }
}
