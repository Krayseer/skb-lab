package ru.krayseer.accountservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.krayseer.messaging.Message;
import ru.krayseer.messaging.MessagingService;

/**
 * Реализация {@link MessagingService} для Kafka
 */
@Service
@RequiredArgsConstructor
public class KafkaMessagingService implements MessagingService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public <T> void send(String queueId, Message<T> message) {
        kafkaTemplate.send(queueId, objectMapper.writeValueAsString(message.getData()));
    }

}
