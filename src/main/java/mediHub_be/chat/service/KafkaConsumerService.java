package mediHub_be.chat.service;

import mediHub_be.chat.KafkaConstants;
import mediHub_be.chat.dto.ResponseChatMessageDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    private final SimpMessagingTemplate messagingTemplate; // STOMP 메시지 전송을 위한 템플릿

    public KafkaConsumerService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Kafka에서 메시지를 수신하고, STOMP 구독자에게 메시지 전송
    @KafkaListener(topics = KafkaConstants.KAFKA_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void consumeMessage(ResponseChatMessageDTO message) {
        String destination = "/subscribe/" + message.getChatroomSeq();  // 채팅방별 구독 경로

        // 해당 채팅방을 구독하고 있는 사용자들에게 메시지 전송
        messagingTemplate.convertAndSend(destination, message);
    }

}
