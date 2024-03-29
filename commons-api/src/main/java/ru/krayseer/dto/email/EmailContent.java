package ru.krayseer.dto.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Информация для сообщения
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailContent<T> {

    /**
     * Тема сообщения
     */
    private String subject;

    /**
     * Данные сообщения
     */
    private T content;

}
