package ru.krayseer.requestprocessingservice;

/**
 * Обработчик запросов
 */
public interface RequestProcessor {

    /**
     * Обработать запрос
     *
     * @param requestData данные для обработки
     */
    <T> ProcessResult processRequest(T requestData);

}
