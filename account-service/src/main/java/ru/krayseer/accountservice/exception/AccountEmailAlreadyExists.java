package ru.krayseer.accountservice.exception;

/**
 * Ошибка, сигнализирующая, что аккаунт с каким-то конкретным почтовым адресом уже существует
 */
public class AccountEmailAlreadyExists extends RuntimeException {

    public AccountEmailAlreadyExists(String email) {
        super(String.format("account with email <%s> already exists", email));
    }

}
