package ru.krayseer.messaging.domain;

/**
 * Название очереди сообщений
 */
public final class MessageQueue {

    /**
     * Очередь обработки заявок на регистрацию пользователей
     */
    public static final String ACCOUNT_PROCESS_REQUEST = "approval-account-request";

    /**
     * Очередь получения обработанных заявок на регистрацию пользователей
     */
    public static final String ACCOUNT_APPROVAL_REQUEST_RESULT ="approval-account-request-result";

    /**
     * Очередь отправки сообщений на email
     */
    public static final String EMAIL_SENDER = "email-sender";

}
