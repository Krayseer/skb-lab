package ru.krayseer.requestprocessingservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class Messages {

    @Value("${app.locale}")
    private String locale;

    private final ResourceBundle resourceBundle;

    public Messages() {
//        Locale appLocale = locale == null ? Locale.getDefault() : Locale.of(locale);
        resourceBundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
    }

    public String getMessageByKey(String key) {
        return resourceBundle.getString(key);
    }

}
