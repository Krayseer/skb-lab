package ru.krayseer.notificationservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.krayseer.dto.email.EmailDTO;
import ru.krayseer.messaging.domain.Message;
import ru.krayseer.messaging.domain.MessageQueue;
import ru.krayseer.notificationservice.service.EmailService;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailMessageListener {

    private final ObjectMapper objectMapper;

    private final EmailService emailService;

    @SneakyThrows
    @KafkaListener(topics = MessageQueue.EMAIL_SENDER, groupId = "account-group")
    public void receive(String message) {
        Message<EmailDTO> emailDTOMessage = objectMapper.readValue(message, new TypeReference<>() {
        });
        EmailDTO emailDTO = emailDTOMessage.getData();
        emailService.sendMessage(emailDTO.getToAddress(), emailDTO.getContent());
    }

}
