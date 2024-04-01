package ru.krayseer.accountservice.service;

import ru.krayseer.accountservice.domain.Task;

/**
 * Сервис обработки заданий
 */
public interface TaskService {

    /**
     * Запуск сервиса
     */
    void start();

    /**
     * Добавление задания на обработку
     */
    void addTask(Task<?> task);

    /**
     * Остановка сервиса
     */
    void stop();

}
