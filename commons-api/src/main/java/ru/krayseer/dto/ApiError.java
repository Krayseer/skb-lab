package ru.krayseer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO с данными об ошибке
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError<T> {

    /**
     * Сообщение об ошибке
     */
    private T data;

}




