package ru.krayseer.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сообщение
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message<T> {

    /**
     * Данные сообщения
     */
    private T data;

}
