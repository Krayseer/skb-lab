package ru.krayseer.requestprocessingservice.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.krayseer.dto.AccountDTO;
import ru.krayseer.requestprocessingservice.RequestProcessor;
import ru.krayseer.requestprocessingservice.ProcessResult;

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
    public <T> ProcessResult processRequest(T requestData) {
        AccountDTO accountDTO = (AccountDTO) requestData;
        log.info("Start handle process account request: {}", accountDTO);
        return accountDTO.getId() != null ? ProcessResult.SUCCESS : ProcessResult.INVALID;
    }

}
