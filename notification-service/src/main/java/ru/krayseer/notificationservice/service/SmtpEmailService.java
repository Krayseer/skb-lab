package ru.krayseer.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.krayseer.dto.email.EmailAddress;
import ru.krayseer.dto.email.EmailContent;
import ru.krayseer.dto.email.EmailDTO;

/**
 * Реализация сервиса отправки сообщений по протоколу SMTP
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmtpEmailService implements EmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void sendMessage(EmailDTO emailDTO) {
        log.info("Send message: {}", emailDTO);
        EmailAddress emailAddress = emailDTO.getToAddress();
        EmailContent<?> emailContent = emailDTO.getContent();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(emailAddress.getAddress());
        message.setSubject(emailContent.getSubject());
        message.setText((String) emailContent.getContent());
        emailSender.send(message);
    }
}
