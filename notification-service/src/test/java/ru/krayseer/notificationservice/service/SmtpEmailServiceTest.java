package ru.krayseer.notificationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import ru.krayseer.dto.email.EmailAddress;
import ru.krayseer.dto.email.EmailContent;
import ru.krayseer.dto.email.EmailDTO;

/**
 * Тесты для класса {@link SmtpEmailService}
 */
@ExtendWith(MockitoExtension.class)
class SmtpEmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private SmtpEmailService emailService;

    /**
     * Тестирование отправки сообщения с данными из {@link EmailDTO}
     */
    @Test
    void sendMessage() {
        EmailDTO emailDTO = new EmailDTO(
                new EmailAddress("address"),
                new EmailContent<>("subject", "content")
        );
        emailService.sendMessage(emailDTO);
        SimpleMailMessage expectedMailMessage = new SimpleMailMessage();
        expectedMailMessage.setFrom(null);
        expectedMailMessage.setTo("address");
        expectedMailMessage.setSubject("subject");
        expectedMailMessage.setText("content");
        Mockito.verify(javaMailSender, Mockito.times(1)).send(expectedMailMessage);
    }

}