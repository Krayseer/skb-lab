package ru.krayseer.accountservice.context;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.krayseer.accountservice.service.TaskService;

/**
 * Слушатель события загрузки приложения
 */
@Component
@RequiredArgsConstructor
public class ApplicationContextInitializeListener implements ApplicationListener<ContextRefreshedEvent> {

    private final TaskService taskService;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        taskService.start();
    }

}
