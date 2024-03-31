package ru.krayseer.accountservice.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.krayseer.dto.AccountDTO;
import ru.krayseer.dto.ApprovedRequestResultDTO;
import ru.krayseer.dto.email.EmailAddress;
import ru.krayseer.dto.email.EmailContent;
import ru.krayseer.dto.email.EmailDTO;
import ru.krayseer.messaging.domain.Message;
import ru.krayseer.messaging.domain.MessageId;
import ru.krayseer.messaging.domain.MessageQueue;
import ru.krayseer.messaging.service.MessagingService;

import java.util.concurrent.ExecutorService;

@ExtendWith(MockitoExtension.class)
class ApprovalRequestServiceTest {

    @Mock
    private ExecutorService executorService;

    @Mock
    private MessagingService messagingService;

    @InjectMocks
    private ApprovalRequestService approvalRequestService;

    @Test
    @SneakyThrows
    void processRequest() {
        AccountDTO accountDTO = new AccountDTO(1L, "login", "email@yandex.ru", "name");
        MessageId messageId = new MessageId("testId");
        Message<AccountDTO> message = new Message<>(MessageQueue.ACCOUNT_PROCESS_REQUEST, accountDTO);
        Message<ApprovedRequestResultDTO> expectedMessage = new Message<>(messageId, "testQueue", new ApprovedRequestResultDTO(true));
        Mockito.when(messagingService.send(message)).thenReturn(messageId);
        Mockito.when(messagingService.receive(messageId, ApprovedRequestResultDTO.class)).thenReturn(expectedMessage);

        approvalRequestService.processRequest(accountDTO);

        EmailDTO expectedEmailDTO = new EmailDTO(
                new EmailAddress("email@yandex.ru"),
                new EmailContent<>(
                        "Approval of application for registration", "you success register in service"
                )
        );
        Message<EmailDTO> expectedEmailMessage = new Message<>(
                messageId, MessageQueue.EMAIL_SENDER, expectedEmailDTO
        );
//        Mockito.verify(messagingService, Mockito.times(1)).send(expectedEmailMessage);
    }

//    @Test
//    void processRequestInvalid() {
//        MessageId messageId = new MessageId("testId");
//        Message<ApprovedRequestResultDTO> message = new Message<>(messageId, "testQueue", new ApprovedRequestResultDTO(true));
//        Mockito.when(messagingService.send(Mockito.any())).thenReturn(messageId);
//        Mockito.when(messagingService.receive(messageId, ApprovedRequestResultDTO.class)).thenReturn(message);
//
//        approvalRequestService.processRequest(Mockito.any());
//
//        Mockito.verify(messagingService, Mockito.never()).send(Mockito.any());
//    }

}