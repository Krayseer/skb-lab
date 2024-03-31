package ru.krayseer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.krayseer.messaging.domain.MessageId;

/**
 * DTO с результатом обработки запроса
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovedRequestResultDTO {

    /**
     * Одобрена ли заявка
     */
    private boolean approved;

}
