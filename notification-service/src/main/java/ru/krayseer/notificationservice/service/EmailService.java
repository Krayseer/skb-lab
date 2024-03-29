package ru.krayseer.notificationservice.service;

import ru.krayseer.dto.email.EmailDTO;

/**
 * Сервис отправки сообщений по email
 */
public interface EmailService {

    /**
     * Отправить сообщение
     *
     * @param emailDTO сообщение
     */
    void sendMessage(EmailDTO emailDTO);

}
