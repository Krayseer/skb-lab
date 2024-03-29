package ru.krayseer.accountservice;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.krayseer.accountservice.domain.Account;

/**
 * Репозиторий для работы с сущностью {@link Account}
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Проверить существование пользователя по адресу электронной почты
     *
     * @param email адрес электронной почты
     */
    boolean existsByEmail(String email);

    /**
     * Проверить существование пользователя по логину
     *
     * @param login логин
     */
    boolean existsByLogin(String login);

}
