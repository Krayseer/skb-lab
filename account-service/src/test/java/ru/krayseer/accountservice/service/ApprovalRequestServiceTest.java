package ru.krayseer.accountservice.service;

import junit.framework.Assert;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.krayseer.dto.AccountDTO;
import ru.krayseer.dto.RequestProcessingDTO;
import ru.krayseer.dto.email.EmailDTO;
import ru.krayseer.messaging.domain.Message;
import ru.krayseer.messaging.domain.MessageId;
import ru.krayseer.messaging.service.MessagingService;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * Тесты для класса {@link ApprovalRequestService}
 */
@ExtendWith(MockitoExtension.class)
class ApprovalRequestServiceTest {

    @Mock
    private MessagingService messagingService;

    @Mock
    private TaskService taskService;

    @Captor
    private ArgumentCaptor<Message<?>> argumentCaptor;

    private ApprovalRequestService approvalRequestService;

    @BeforeEach
    public void setUp() {
        approvalRequestService = new ApprovalRequestService(
                taskService, Executors.newSingleThreadExecutor(), messagingService
        );
    }

    /**
     * Проверка успешной обработки заявки на регистрацию аккаунта с отправкой сообщения пользователю на почту
     */
    @Test
    @SneakyThrows
    void processSuccessRequest() {
        MessageId messageId = new MessageId("testId");
        Message<RequestProcessingDTO> resultMessage = new Message<>(
                messageId, "receive-result", new RequestProcessingDTO(true)
        );
        Mockito.when(messagingService.send(Mockito.any())).thenReturn(messageId);
        AccountDTO accountDTO = new AccountDTO(1L, "login", "email@gmail.com", "name");
        Mockito.when(messagingService.receive(Mockito.eq(messageId), Mockito.eq(RequestProcessingDTO.class)))
                .thenReturn(resultMessage);

        approvalRequestService.processRequest(accountDTO);
        Thread.sleep(500);

        Mockito.verify(messagingService, Mockito.atLeastOnce()).send(argumentCaptor.capture());
        Assert.assertEquals(2, argumentCaptor.getAllValues().size());
        Message<?> sendMessage = argumentCaptor.getAllValues().get(1);
        Assertions.assertThat(sendMessage.getData()).isInstanceOf(EmailDTO.class);
        EmailDTO sendEmailDTO = (EmailDTO) sendMessage.getData();
        Assertions.assertThat(sendEmailDTO.getEmailAddress().getAddress()).isEqualTo("email@gmail.com");
        Assertions.assertThat(sendEmailDTO.getEmailContent().getSubject()).isEqualTo(
                "Result of approval of the registration application"
        );
        Assertions.assertThat(sendEmailDTO.getEmailContent().getContent()).isEqualTo(
                "you success register in service"
        );
    }

    /**
     * Проверка безуспешной обработки заявки на регистрацию аккаунта с отправкой сообщения пользователю на почту
     */
    @Test
    @SneakyThrows
    void processInvalidRequest() {
        MessageId messageId = new MessageId("testId");
        Message<RequestProcessingDTO> resultMessage = new Message<>(
                messageId, "receive-result", new RequestProcessingDTO(false)
        );
        Mockito.when(messagingService.send(Mockito.any())).thenReturn(messageId);
        AccountDTO accountDTO = new AccountDTO(1L, "login", "email@gmail.com", "name");
        Mockito.when(messagingService.receive(Mockito.eq(messageId), Mockito.eq(RequestProcessingDTO.class)))
                .thenReturn(resultMessage);

        approvalRequestService.processRequest(accountDTO);
        Thread.sleep(500);

        Mockito.verify(messagingService, Mockito.atLeastOnce()).send(argumentCaptor.capture());
        Assert.assertEquals(2, argumentCaptor.getAllValues().size());
        Message<?> sendMessage = argumentCaptor.getAllValues().get(1);
        Assertions.assertThat(sendMessage.getData()).isInstanceOf(EmailDTO.class);
        EmailDTO sendEmailDTO = (EmailDTO) sendMessage.getData();
        Assertions.assertThat(sendEmailDTO.getEmailAddress().getAddress()).isEqualTo("email@gmail.com");
        Assertions.assertThat(sendEmailDTO.getEmailContent().getSubject()).isEqualTo(
                "Result of approval of the registration application"
        );
        Assertions.assertThat(sendEmailDTO.getEmailContent().getContent()).isEqualTo(
                "your registration request has not been approved"
        );
    }

    /**
     * Тестирование отправки сообщения на обработку при выброшенной ошибке {@link TimeoutException}.
     */
    @Test
    @SneakyThrows
    void processRequestWithTimeoutException() {
        Mockito.when(messagingService.receive(Mockito.any(), Mockito.any())).thenThrow(TimeoutException.class);

        approvalRequestService.processRequest(new AccountDTO());
        Thread.sleep(500);

        // Задание отправилось в сервис обработки заданий в фоновом режиме
        Mockito.verify(taskService, Mockito.times(1)).addTask(Mockito.any());
    }

}