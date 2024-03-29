package ru.krayseer.accountservice.exception;

/**
 * Ошибка, сигнализирующая, что аккаунт с каким-то конкретным логином уже существует
 */
public class AccountLoginAlreadyExists extends RuntimeException {

    public AccountLoginAlreadyExists(String login) {
        super(String.format("account with login <%s> already exists", login));
    }

}
