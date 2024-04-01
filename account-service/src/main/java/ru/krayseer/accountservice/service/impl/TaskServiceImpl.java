package ru.krayseer.accountservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.krayseer.accountservice.domain.Task;
import ru.krayseer.accountservice.service.TaskService;

import java.util.concurrent.*;

/**
 * Реализация сервиса обработки заданий.
 * Содержит в себе планировщик, который каждые 5 минут обрабатывает задания из очереди
 */
@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    /**
     * Количество потоков, использующихся в планировщике
     */
    private static final int SCHEDULER_THREAD_COUNT = 1;

    /**
     * Количество потоков, использующихся при обработке заданий
     */
    private static final int EXECUTOR_THREAD_COUNT = 2;

    /**
     * Время задержки между выполнением заданий из очереди в секундах
     */
    private static final int SCHEDULER_DELAY = 300;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(SCHEDULER_THREAD_COUNT);

    private final ExecutorService executorService = Executors.newScheduledThreadPool(EXECUTOR_THREAD_COUNT);

    /**
     * Очередь заданий
     */
    private final BlockingQueue<Task<?>> taskQueue = new LinkedBlockingQueue<>();

    /**
     * Запущен ли сервис
     */
    private boolean running = false;

    @Override
    public void start() {
        if (!running) {
            scheduler.scheduleAtFixedRate(this::process, 0, SCHEDULER_DELAY, TimeUnit.SECONDS);
            running = true;
            log.info("TaskService started.");
        } else {
            log.warn("TaskService is already running.");
        }
    }

    @Override
    public void addTask(Task<?> task) {
        taskQueue.add(task);
    }

    @Override
    public void stop() {
        scheduler.shutdown();
        executorService.shutdown();
    }

    /**
     * Запуск процесса обработки заданий из очереди
     */
    private void process() {
        while (!taskQueue.isEmpty()) {
            Task<?> task = taskQueue.poll();
            if (task != null) {
                log.info("Start processing task: {}", task);
                try {
                    executorService.execute(() -> {
                        try {
                            task.complete();
                        } catch (Exception ex) {
                            log.error("Error processing task: {}", task);
                        }
                    });
                } catch (RejectedExecutionException ex) {
                    log.error("Failed to submit task for execution: {}", task);
                }
            }
        }
    }

}
