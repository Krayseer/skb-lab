package ru.krayseer.accountservice.context;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import ru.krayseer.accountservice.service.TaskService;

/**
 * Слушатель события остановки приложения
 */
@Component
@RequiredArgsConstructor
public class ApplicationContextCloseListener implements ApplicationListener<ContextClosedEvent> {

    private final TaskService taskService;

    @Override
    public void onApplicationEvent(@NonNull ContextClosedEvent event) {
        taskService.stop();
    }

}
