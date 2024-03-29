package ru.krayseer.messaging;

/**
 * Название очереди сообщений
 */
public final class MessageQueue {

    /**
     * Очередь обработки заявок регистрации пользователей
     */
    public static final String ACCOUNT_PROCESS_REQUEST = "process-account-request";

    /**
     * Очередь отправки сообщений на email
     */
    public static final String EMAIL_SENDER = "email-sender";

}
