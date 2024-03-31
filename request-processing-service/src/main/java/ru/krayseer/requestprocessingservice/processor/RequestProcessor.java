package ru.krayseer.requestprocessingservice.processor;

/**
 * Обработчик запросов
 */
public interface RequestProcessor {

    /**
     * Обработать запрос
     *
     * @param requestData запрос с данными для обработки
     *
     * @return результат обработки запроса (одобрен/не одобрен)
     */
    <T> boolean approveRequest(T requestData);

}
