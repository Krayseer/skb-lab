package ru.krayseer.requestprocessingservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.krayseer.dto.AccountDTO;
import ru.krayseer.messaging.MessageQueue;
import ru.krayseer.requestprocessingservice.ProcessResult;
import ru.krayseer.requestprocessingservice.RequestProcessor;
import ru.krayseer.requestprocessingservice.account.AccountResultSender;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final ObjectMapper objectMapper;

    private final RequestProcessor requestProcessor;

    private final AccountResultSender accountResultSender;

    @SneakyThrows
    @KafkaListener(topics = MessageQueue.ACCOUNT_PROCESS_REQUEST, groupId = "account-group")
    public void listen(String message) {
        AccountDTO accountDTO = objectMapper.readValue(message, AccountDTO.class);
        ProcessResult processResult = requestProcessor.processRequest(accountDTO);
        accountResultSender.sendMessage(accountDTO.getEmail(), processResult);
    }

}
