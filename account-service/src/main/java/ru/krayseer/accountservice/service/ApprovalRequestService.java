package ru.krayseer.accountservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.krayseer.accountservice.domain.Task;
import ru.krayseer.dto.AccountDTO;
import ru.krayseer.dto.RequestProcessingDTO;
import ru.krayseer.dto.email.EmailAddress;
import ru.krayseer.dto.email.EmailContent;
import ru.krayseer.dto.email.EmailDTO;
import ru.krayseer.messaging.domain.Message;
import ru.krayseer.messaging.domain.MessageId;
import ru.krayseer.messaging.domain.MessageQueue;
import ru.krayseer.messaging.service.MessagingService;

import java.util.concurrent.*;

/**
 * Сервис обработки регистрационных заявок пользователей
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApprovalRequestService {

    private final TaskService taskService;

    private final ExecutorService executorService;

    private final MessagingService messagingService;

    /**
     * Отправка данных аккаунта на одобрение в сервис одобрения заявок. Если при ожидании ответа ловится ошибка, то
     * создаем отдельное {@link Task задание} и отправляем его в {@link TaskService сервис обработки заданий}
     *
     * @param accountDTO данные об аккаунте
     */
    public void processRequest(AccountDTO accountDTO) {
        Message<AccountDTO> message = new Message<>(MessageQueue.ACCOUNT_PROCESS_REQUEST, accountDTO);
        MessageId messageId = messagingService.send(message);
        executorService.execute(() -> {
            Message<RequestProcessingDTO> processResult;
            try {
                processResult = messagingService.receive(messageId, RequestProcessingDTO.class);
                sendEmailWithRegisterResult(accountDTO.getEmail(), processResult.getData());
            } catch (TimeoutException e) {
                taskService.addTask(new Task<>(accountDTO, this::processRequest));
            }
        });
    }

    /**
     * Отправить сообщение пользователю с результатом проверки
     *
     * @param requestProcessingDTO результат одобрения заявки
     */
    private void sendEmailWithRegisterResult(String email, RequestProcessingDTO requestProcessingDTO) {
        EmailDTO emailDTO = new EmailDTO(
                new EmailAddress(email),
                new EmailContent<>(
                        "Result of approval of the registration application",
                        getMessageByRequestProcessingResult(requestProcessingDTO.isApproved())
                )
        );
        Message<EmailDTO> requestProcessingResultMessage = new Message<>(MessageQueue.EMAIL_SENDER, emailDTO);
        messagingService.send(requestProcessingResultMessage);
        log.info("Send message with registration result: {}", requestProcessingResultMessage);
    }

    /**
     * Получить сообщение в зависимости от результата одобрения или отклонения заявки на регистрацию аккаунта
     *
     * @param isAccountApproved одобрена ли заявка
     */
    private String getMessageByRequestProcessingResult(boolean isAccountApproved) {
        return isAccountApproved
                ? "you success register in service"
                : "your registration request has not been approved";
    }

}
