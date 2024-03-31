package ru.krayseer.requestprocessingservice.account;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.krayseer.dto.AccountDTO;
import ru.krayseer.requestprocessingservice.processor.AccountRequestProcessor;
import ru.krayseer.requestprocessingservice.processor.RequestProcessor;

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
        Assertions.assertEquals(true, accountRequestProcessor.approveRequest(accountDTO));
    }

    /**
     * Обработка запроса с положительным ответом
     */
    @Test
    void processRequestWithInvalidData() {
        AccountDTO accountDTO = new AccountDTO(null, "login", "email", "password");
        Assertions.assertEquals(false, accountRequestProcessor.approveRequest(accountDTO));
    }

}