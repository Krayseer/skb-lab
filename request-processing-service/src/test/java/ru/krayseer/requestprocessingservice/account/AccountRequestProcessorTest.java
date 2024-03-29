package ru.krayseer.requestprocessingservice.account;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.krayseer.dto.AccountDTO;
import ru.krayseer.requestprocessingservice.ProcessResult;
import ru.krayseer.requestprocessingservice.RequestProcessor;

/**
 * Тесты для класса {@link AccountRequestProcessor}
 */
class AccountRequestProcessorTest {

    private final RequestProcessor accountRequestProcessor = new AccountRequestProcessor();

    /**
     * Обработка запроса с отрицательным ответом
     */
    @Test
    void processRequestWithValidData() {
        AccountDTO accountDTO = new AccountDTO(1L, "login", "email", "password");
        Assertions.assertEquals(ProcessResult.SUCCESS, accountRequestProcessor.processRequest(accountDTO));
    }

    /**
     * Обработка запроса с положительным ответом
     */
    @Test
    void processRequestWithInvalidData() {
        AccountDTO accountDTO = new AccountDTO(null, "login", "email", "password");
        Assertions.assertEquals(ProcessResult.INVALID, accountRequestProcessor.processRequest(accountDTO));
    }

}