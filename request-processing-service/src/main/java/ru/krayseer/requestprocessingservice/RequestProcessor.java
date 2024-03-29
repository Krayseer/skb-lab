package ru.krayseer.requestprocessingservice;

/**
 * Обработчик запросов
 */
public interface RequestProcessor {

    /**
     * Обработать запрос
     *
     * @param requestData запрос с данными для обработки
     */
    <T> ProcessResult processRequest(T requestData);

}
