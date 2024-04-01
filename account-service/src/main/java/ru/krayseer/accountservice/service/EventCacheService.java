package ru.krayseer.accountservice.service;

import org.springframework.stereotype.Service;
import ru.krayseer.messaging.domain.Message;
import ru.krayseer.messaging.domain.MessageId;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * Сервис, обрабатывающий отправленные события
 */
@Service
public class EventCacheService {

    private final Map<MessageId, CompletableFuture<Object>> eventByMessageId = new ConcurrentHashMap<>();

    /**
     * Добавить событие, посланное на обработку, в кэш
     *
     * @param messageId идентификатор сообщения
     */
    public CompletableFuture<?> addEvent(MessageId messageId) {
        CompletableFuture<Object> completableFuture = new CompletableFuture<>();
        eventByMessageId.put(messageId, completableFuture);
        return completableFuture;
    }

    /**
     * Сообщиить о выполнении события
     */
    public void completeEvent(Message<?> message) {
        CompletableFuture<Object> future = eventByMessageId.get(message.getMessageId());
        if (future == null) {
            return;
        }
        future.complete(message.getData());
    }

    /**
     * Обработать событие в кэше
     *
     * @param messageId идентификатор сообщения
     */
    public Runnable processEvent(MessageId messageId) {
        return () -> {
            CompletableFuture<?> future = eventByMessageId.get(messageId);
            if (future != null && !future.isDone()) {
                future.completeExceptionally(new TimeoutException("Timeout while waiting for response"));
            }
            eventByMessageId.remove(messageId);
        };
    }

}
