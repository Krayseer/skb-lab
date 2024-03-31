package ru.krayseer.messaging.service;

import ru.krayseer.messaging.domain.Message;
import ru.krayseer.messaging.domain.MessageId;

import java.util.concurrent.TimeoutException;

/**
 * Сервис обработки сообщений
 */
public interface MessagingService {

    /**
     * Отправка сообщения в шину.
     *
     * @param message сообщение для отправки.
     *
     * @return идентификатор отправленного сообщения (correlationId)
     */
    <T> MessageId send(Message<T> message);

    /**
     * Встает на ожидание ответа по сообщению с messageId. Редко, но может кинуть исключение по таймауту.
     *
     * @param messageId     идентификатор сообщения, на которое ждем ответ
     * @param messageType   тип сообщения, к которому необходимо привести ответ
     *
     * @return тело ответа
     */
    <T> Message<T> receive(MessageId messageId, Class<T> messageType) throws TimeoutException;

}
