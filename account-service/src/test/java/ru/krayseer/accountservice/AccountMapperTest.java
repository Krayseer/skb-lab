package ru.krayseer.accountservice;

import junit.framework.Assert;
import org.junit.jupiter.api.Test;
import ru.krayseer.accountservice.domain.Account;
import ru.krayseer.accountservice.domain.RegisterDTO;
import ru.krayseer.dto.AccountDTO;

/**
 * Тесты для класса {@link AccountMapper}
 */
class AccountMapperTest {

    /**
     * Тест метода {@link AccountMapper#createFrom(RegisterDTO)}
     */
    @Test
    void createFrom() {
        String login = "login";
        String password = "password";
        String email = "email";
        String name = "name";
        RegisterDTO registerDTO = new RegisterDTO(login, password, email, name);
        Account expectedAccount = new Account(null, login, password, email, name);
        Account actualAccount = AccountMapper.createFrom(registerDTO);
        Assert.assertTrue(isAccountsEquals(expectedAccount, actualAccount));
    }

    /**
     * Тест метода {@link AccountMapper#createTo(Account)}
     */
    @Test
    void createTo() {
        String login = "login";
        String password = "password";
        String email = "email";
        String name = "name";
        Account account = new Account(1L, login, password, email, name);
        AccountDTO expectedDTO = new AccountDTO(1L, login, email, name);
        AccountDTO actualDTO = AccountMapper.createTo(account);
        Assert.assertEquals(expectedDTO, actualDTO);
    }

    /**
     * Проверка идентичности аккаунтов
     *
     * @param firstAccount  первый аккаунт
     * @param secondAccount второй аккаунт
     */
    private boolean isAccountsEquals(Account firstAccount, Account secondAccount) {
        return firstAccount.getLogin().equals(secondAccount.getLogin())
                && firstAccount.getPassword().equals(secondAccount.getPassword())
                && firstAccount.getEmail().equals(secondAccount.getEmail())
                && firstAccount.getName().equals(secondAccount.getName());
    }

}