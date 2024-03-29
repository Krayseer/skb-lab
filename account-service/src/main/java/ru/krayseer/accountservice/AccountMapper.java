package ru.krayseer.accountservice;

import ru.krayseer.dto.AccountDTO;
import ru.krayseer.accountservice.domain.Account;
import ru.krayseer.accountservice.domain.RegisterDTO;

/**
 * Маппер, занимающийся преобразованием из DTO в Account и наоборот
 */
public final class AccountMapper {

    /**
     * Создание сущности {@link Account} из регистрационных данных
     *
     * @param registerDTO регистрационные данные об аккаунте
     */
    public static Account createFrom(RegisterDTO registerDTO) {
        return Account.builder()
                .login(registerDTO.getLogin())
                .password(registerDTO.getPassword())
                .email(registerDTO.getEmail())
                .name(registerDTO.getName())
                .build();
    }

    /**
     * Создание DTO с информацией об {@link Account аккаунте}
     *
     * @param account аккаунт, информацю о котором нужно получить
     */
    public static AccountDTO createTo(Account account) {
        return new AccountDTO(account.getId(), account.getLogin(), account.getEmail(), account.getName());
    }

}
