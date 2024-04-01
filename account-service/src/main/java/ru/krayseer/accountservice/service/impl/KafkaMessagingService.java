package ru.krayseer.accountservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.krayseer.accountservice.service.EventCacheService;
import ru.krayseer.dto.RequestProcessingDTO;
import ru.krayseer.messaging.service.MessagingService;
import ru.krayseer.messaging.domain.Message;
import ru.krayseer.messaging.domain.MessageId;
import ru.krayseer.messaging.domain.MessageQueue;

import java.util.concurrent.*;

/**
 * Сервис обработки сообщений, в реализации на Kafka
 */
@Service
@RequiredArgsConstructor
public class KafkaMessagingService implements MessagingService {

    /**
     * Время ожидания сообщения в секундах
     */
    private static final int DELAY_WAITING_MESSAGE = 3;

    /**
     * Количество потоков для {@link ExecutorService}
     */
    private static final int EXECUTOR_THREAD_COUNT = 2;

    private final ObjectMapper objectMapper;

    private final EventCacheService eventCacheService;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(EXECUTOR_THREAD_COUNT);

    @Override
    @SneakyThrows
    public <T> MessageId send(Message<T> message) {
        kafkaTemplate.send(message.getQueue(), objectMapper.writeValueAsString(message));
        return message.getMessageId();
    }

    @Override
    public <T> Message<T> receive(MessageId messageId, Class<T> messageType) throws TimeoutException {
        CompletableFuture<?> completableFuture = eventCacheService.addEvent(messageId);
        executorService.schedule(eventCacheService.processEvent(messageId), DELAY_WAITING_MESSAGE, TimeUnit.SECONDS);
        try {
            T messageData = messageType.cast(completableFuture.get());
            return new Message<>(messageId, MessageQueue.RECEIVE_RESULT, messageData);
        } catch (InterruptedException e) {
            throw new RuntimeException("threading exception");
        } catch (ExecutionException e) {
            throw new TimeoutException();
        }
    }

    @SneakyThrows
    @KafkaListener(topics = MessageQueue.ACCOUNT_APPROVAL_REQUEST_RESULT, groupId = "account-group")
    public void listen(String receivedMessage) {
        Message<RequestProcessingDTO> message = objectMapper.readValue(receivedMessage, new TypeReference<>() {
        });
        eventCacheService.completeEvent(message);
    }

}
