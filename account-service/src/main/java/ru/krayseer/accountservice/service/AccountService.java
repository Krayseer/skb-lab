package ru.krayseer.accountservice.service;

import ru.krayseer.dto.AccountDTO;
import ru.krayseer.accountservice.domain.RegisterDTO;

/**
 * Сервис обработки аккаунтов
 */
public interface AccountService {

    /**
     * Обработать запрос на регистрацию аккаунта
     *
     * @param registerDTO данные об аккаунте, который нужно зарегистрировать
     * @return данные о зарегистрированном аккаунте
     */
    AccountDTO handleRegisterAccountRequest(RegisterDTO registerDTO);

}
