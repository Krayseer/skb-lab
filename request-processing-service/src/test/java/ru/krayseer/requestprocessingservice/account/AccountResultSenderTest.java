package ru.krayseer.requestprocessingservice.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.krayseer.dto.email.EmailAddress;
import ru.krayseer.dto.email.EmailContent;
import ru.krayseer.dto.email.EmailDTO;
import ru.krayseer.messaging.Message;
import ru.krayseer.messaging.MessagingService;
import ru.krayseer.requestprocessingservice.Messages;
import ru.krayseer.requestprocessingservice.ProcessResult;

/**
 * Тесты для класса {@link AccountResultSender}
 */
@ExtendWith(MockitoExtension.class)
class AccountResultSenderTest {

    @Mock
    private Messages messages;

    @Mock
    private MessagingService messagingService;

    @InjectMocks
    private AccountResultSender accountResultSender;

    /**
     * Проверка отправки сообщения с успешным результатом обработки
     */
    @Test
    void sendSuccessMessage() {
        Mockito.when(messages.getMessageByKey(Mockito.any())).thenReturn("subject", "successContent");
        accountResultSender.sendMessage("test@email.com", ProcessResult.SUCCESS);
        Message<EmailDTO> expectedMessage = new Message<>(
                new EmailDTO(
                        new EmailAddress("test@email.com"),
                        new EmailContent<>("subject", "successContent")
                )
        );
        Mockito.verify(messagingService, Mockito.times(1)).send("email-sender", expectedMessage);
    }

    /**
     * Проверка отправки сообщения с безуспешным результатом обработки
     */
    @Test
    void sendInvalidMessage() {
        Mockito.when(messages.getMessageByKey(Mockito.any())).thenReturn("subject", "invalidContent");
        accountResultSender.sendMessage("test@email.com", ProcessResult.INVALID);
        Message<EmailDTO> expectedMessage = new Message<>(
                new EmailDTO(
                        new EmailAddress("test@email.com"),
                        new EmailContent<>("subject", "invalidContent")
                )
        );
        Mockito.verify(messagingService, Mockito.times(1)).send("email-sender", expectedMessage);
    }

}