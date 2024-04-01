package ru.krayseer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO с результатом обработки запроса
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestProcessingDTO {

    /**
     * Одобрена ли заявка
     */
    private boolean approved;

}
