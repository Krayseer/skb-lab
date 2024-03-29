package ru.krayseer.requestprocessingservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Сообщения приложения
 */
@Component
public class Messages {

    @Value("${app.locale}")
    private String locale;

    private final ResourceBundle resourceBundle;

    public Messages() {
        resourceBundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
    }

    /**
     * Получить сообщение по ключу из {@link #resourceBundle}
     *
     * @param key ключ сообщения
     */
    public String getMessageByKey(String key) {
        return resourceBundle.getString(key);
    }

}
