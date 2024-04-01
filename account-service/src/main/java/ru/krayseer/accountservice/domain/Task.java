package ru.krayseer.accountservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.Consumer;

/**
 * Задание
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task<T> {

    /**
     * Объект, который должен быть исполнен
     */
    private T data;

    /**
     * Обработчик объекта
     */
    private Consumer<T> dataHandler;

    /**
     * Обработать задание
     */
    public void complete() {
        dataHandler.accept(data);
    }

}
