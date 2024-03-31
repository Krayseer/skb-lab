package ru.krayseer.messaging.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Идентификатор сообщения
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageId {

    /**
     * Идентификатор
     */
    private String id;

}
