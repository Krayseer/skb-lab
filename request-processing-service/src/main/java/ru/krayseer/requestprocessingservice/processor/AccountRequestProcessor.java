package ru.krayseer.requestprocessingservice.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.krayseer.dto.AccountDTO;

/**
 * Обработчик запросов регистрации аккаунтов
 */
@Slf4j
@Service
public class AccountRequestProcessor implements RequestProcessor {

    /**
     * Реализация-заглушка
     */
    @Override
    public <T> boolean approveRequest(T requestData) {
        AccountDTO accountDTO = (AccountDTO) requestData;
        if (accountDTO.getId() == null) {
            log.info("Account invalid approved: {}", accountDTO);
            return false;
        }
        log.info("Account success approved: {}", accountDTO);
        return true;
    }

}
