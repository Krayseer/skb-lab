package ru.krayseer.accountservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.krayseer.dto.AccountDTO;
import ru.krayseer.dto.ApprovedRequestResultDTO;
import ru.krayseer.dto.email.EmailAddress;
import ru.krayseer.dto.email.EmailContent;
import ru.krayseer.dto.email.EmailDTO;
import ru.krayseer.messaging.domain.Message;
import ru.krayseer.messaging.domain.MessageId;
import ru.krayseer.messaging.domain.MessageQueue;
import ru.krayseer.messaging.service.MessagingService;

import java.util.List;
import java.util.concurrent.*;

/**
 * Сервис обработки регистрационных заявок пользователей
 */
@Slf4j
@Service
public class ApprovalRequestService {

    private final ExecutorService executorService;

    private final MessagingService messagingService;

    private final List<AccountDTO> cache = new CopyOnWriteArrayList<>();

    public ApprovalRequestService(ExecutorService executorService,
                                  MessagingService messagingService) {
        this.executorService = executorService;
        this.messagingService = messagingService;
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::scheduling, 0, 3, TimeUnit.SECONDS);
    }

    /**
     * Отправка данных аккаунта на одобрение в сервис одобрения заявок
     *
     * @param accountDTO данные об аккаунте
     */
    public void processRequest(AccountDTO accountDTO) {
        Message<AccountDTO> message = new Message<>(MessageQueue.ACCOUNT_PROCESS_REQUEST, accountDTO);
        MessageId messageId = messagingService.send(message);
        executorService.execute(() -> {
            Message<ApprovedRequestResultDTO> processResult;
            try {
                processResult = messagingService.receive(messageId, ApprovedRequestResultDTO.class);
                cache.remove(accountDTO);
                sendEmailMessageWithApprovalResult(accountDTO.getEmail(), processResult);
            } catch (TimeoutException e) {
                if (cache.contains(accountDTO)) {
                    return;
                }
                cache.add(accountDTO);
            }
        });
    }

    /**
     * Отправить сообщение пользователю с результатом проверки
     *
     * @param message сообщение с результатом проверки
     */
    private void sendEmailMessageWithApprovalResult(String email, Message<ApprovedRequestResultDTO> message) {
        EmailDTO emailDTO = new EmailDTO(
                new EmailAddress(email),
                new EmailContent<>(
                        "Approval of application for registration",
                        getMessageByApprovedAccountResult(message.getData().isApproved())
                )
        );
        messagingService.send(new Message<>(message.getMessageId(), MessageQueue.EMAIL_SENDER, emailDTO));
        log.info("process result: {}", message);
    }

    /**
     * Получить сообщение в зависимости от результата одобрения или отклонения заявки на регистрацию аккаунта
     *
     * @param isAccountApproved одобрена ли заявка
     */
    private String getMessageByApprovedAccountResult(boolean isAccountApproved) {
        return isAccountApproved
                ? "you success register in service"
                : "your registration request has not been approved";
    }

    private void scheduling() {
        cache.forEach(accountDTO -> {
            log.info("Re-processing request: {}", accountDTO);
            processRequest(accountDTO);
        });
    }

}
