package ru.krayseer.requestprocessingservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.krayseer.dto.AccountDTO;
import ru.krayseer.dto.ApprovedRequestResultDTO;
import ru.krayseer.messaging.domain.Message;
import ru.krayseer.messaging.domain.MessageQueue;
import ru.krayseer.messaging.service.MessagingService;
import ru.krayseer.messaging.domain.MessageId;
import ru.krayseer.requestprocessingservice.processor.RequestProcessor;

import java.util.concurrent.TimeoutException;

/**
 * Реализация {@link MessagingService} для Kafka
 */
@Service
@RequiredArgsConstructor
public class KafkaMessagingService implements MessagingService {

    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final RequestProcessor requestProcessor;

    private final MessagingService messagingService;

    @Override
    @SneakyThrows
    public <T> MessageId send(Message<T> message) {
        kafkaTemplate.send(message.getQueue(), objectMapper.writeValueAsString(message));
        return message.getMessageId();
    }

    @Override
    public <T> Message<T> receive(MessageId messageId, Class<T> messageType) throws TimeoutException {
        return null;
    }

    @SneakyThrows
    @KafkaListener(topics = MessageQueue.ACCOUNT_PROCESS_REQUEST, groupId = "account-group")
    public void listen(String accountRequest) {
        Message<AccountDTO> message = objectMapper.readValue(accountRequest, new TypeReference<>() {
        });
        boolean isRequestApproved = requestProcessor.approveRequest(message.getData());
        messagingService.send(new Message<>(
                message.getMessageId(), MessageQueue.ACCOUNT_APPROVAL_REQUEST_RESULT,
                new ApprovedRequestResultDTO(isRequestApproved)
        ));
    }

}
