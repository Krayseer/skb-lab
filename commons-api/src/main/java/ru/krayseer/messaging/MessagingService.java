package ru.krayseer.messaging;

/**
 * Сервис обработки сообщений
 */
public interface MessagingService {

    /**
     * Отправка сообщения в шину.
     */
    <T> void send(String queueId, Message<T> message);

}
