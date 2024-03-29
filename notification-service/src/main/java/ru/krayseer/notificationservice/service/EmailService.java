package ru.krayseer.notificationservice.service;

import ru.krayseer.dto.email.EmailAddress;
import ru.krayseer.dto.email.EmailContent;

/**
 * Сервис отправки сообщений по email
 */
public interface EmailService {

    /**
     * Отправить сообщение
     *
     * @param emailAddress  адрес, куда нужно отправить сообщение
     * @param emailContent  данные сообщения
     */
    void sendMessage(EmailAddress emailAddress, EmailContent<?> emailContent);

}
