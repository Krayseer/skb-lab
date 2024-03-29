package ru.krayseer.requestprocessingservice.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.krayseer.dto.email.EmailDTO;
import ru.krayseer.dto.email.EmailAddress;
import ru.krayseer.dto.email.EmailContent;
import ru.krayseer.messaging.Message;
import ru.krayseer.messaging.MessageQueue;
import ru.krayseer.messaging.MessagingService;
import ru.krayseer.requestprocessingservice.Messages;
import ru.krayseer.requestprocessingservice.ProcessResult;

/**
 * Отправитель сообщений с результатами обработки заявок на регистрацию аккаунтов
 */
@Component
@RequiredArgsConstructor
public class AccountResultSender {

    private final Messages messages;

    private final MessagingService messagingService;

    /**
     * Отправить сообщение пользователю с информацией о результате обработки запроса
     *
     * @param email         адрес, куда нужно отправить сообщение
     * @param processResult результат обработки запроса
     */
    public void sendMessage(String email, ProcessResult processResult) {
        String textKey = getMessageKeyByProcessResult(processResult);
        EmailDTO emailDTO  = new EmailDTO(
                new EmailAddress(email),
                new EmailContent<>(
                        messages.getMessageByKey("request.process.account.result.mail.subject"), messages.getMessageByKey(textKey)
                )
        );
        messagingService.send(MessageQueue.EMAIL_SENDER, new Message<>(emailDTO));
    }

    /**
     * Получить сообщение в зависимости от результата обработки запроса
     *
     * @param processResult результат обработки запроса
     */
    private String getMessageKeyByProcessResult(ProcessResult processResult) {
        return String.format("request.process.account.result.%s", processResult);
    }

}
