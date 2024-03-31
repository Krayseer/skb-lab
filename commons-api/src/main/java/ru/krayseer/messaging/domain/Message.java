package ru.krayseer.messaging.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Сообщение
 */
@Data
@NoArgsConstructor
public class Message<T> {

    /**
     * Идентификатор сообщения
     */
    private MessageId messageId;

    /**
     * Очередь, куда нужно отправить сообщение
     */
    private String queue;

    /**
     * Данные сообщения
     */
    private T data;

    public Message(String queue, T data) {
        this(new MessageId(UUID.randomUUID().toString()), queue, data);
    }

    public Message(MessageId messageId, String queue, T data) {
        this.messageId = messageId;
        this.queue = queue;
        this.data = data;
    }

}
