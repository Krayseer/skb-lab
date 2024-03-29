package ru.krayseer.requestprocessingservice;

/**
 * Отправитель сообщений результатов обработки пользователям
 */
public interface ResultSender {

    /**
     * Отправить сообщение пользователю с информацией о результате обработки запроса
     *
     * @param email         адрес, куда нужно отправить сообщение
     * @param processResult результат обработки запроса
     */
    void sendMessage(String email, ProcessResult processResult);

}
