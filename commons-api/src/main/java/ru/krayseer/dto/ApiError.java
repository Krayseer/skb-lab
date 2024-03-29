package ru.krayseer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO с данными об ошибке
 */
@Data
@NoArgsConstructor
public class ApiError<T> {

    /**
     * Сообщение об ошибке
     */
    private T data;

    public ApiError(T data) {
        this.data = data;
    }

}




