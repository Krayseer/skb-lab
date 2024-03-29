package ru.krayseer.notificationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.krayseer.dto.email.EmailDTO;
import ru.krayseer.messaging.MessageQueue;
import ru.krayseer.notificationservice.service.EmailService;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final EmailService emailService;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @KafkaListener(topics = MessageQueue.EMAIL_SENDER, groupId = "account-group")
    public void receive(String message) {
        EmailDTO emailDTO = objectMapper.readValue(message, EmailDTO.class);
        emailService.sendMessage(emailDTO);
    }

}
