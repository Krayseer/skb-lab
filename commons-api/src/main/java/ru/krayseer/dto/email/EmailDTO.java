package ru.krayseer.dto.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Данные о почтовом сообщении
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {

    /**
     * Адрес, куда нужно отправить сообщение
     */
    private EmailAddress emailAddress;

    /**
     * Данные, которые нужно отправить в сообщении
     */
    private EmailContent<?> emailContent;

}
