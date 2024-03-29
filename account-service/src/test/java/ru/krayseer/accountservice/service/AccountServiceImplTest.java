package ru.krayseer.accountservice.service;

import junit.framework.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.krayseer.accountservice.exception.AccountLoginAlreadyExists;
import ru.krayseer.dto.AccountDTO;
import ru.krayseer.messaging.MessagingService;
import ru.krayseer.accountservice.AccountRepository;
import ru.krayseer.accountservice.domain.Account;
import ru.krayseer.accountservice.domain.RegisterDTO;
import ru.krayseer.accountservice.exception.AccountEmailAlreadyExists;

/**
 * Тесты для сервиса обработки аккаунтов {@link AccountService}
 */
@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MessagingService messagingService;

    @InjectMocks
    private AccountServiceImpl accountService;

    /**
     * Тест успешного сохранения аккаунта
     */
    @Test
    void saveAccount() {
        RegisterDTO validDTO = new RegisterDTO("login", "password", "email", "name");
        Mockito.when(accountRepository.save(Mockito.any())).thenReturn(
                new Account(1L, validDTO.getLogin(), validDTO.getPassword(), validDTO.getEmail(), validDTO.getName())
        );
        AccountDTO actualAccountDTO = accountService.handleRegisterAccountRequest(validDTO);
        AccountDTO expectedAccountDTO = new AccountDTO(1L, "login", "email", "name");
        Assert.assertEquals(expectedAccountDTO, actualAccountDTO);
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(messagingService, Mockito.times(1)).send(Mockito.any(), Mockito.any());
    }

    /**
     * Тест сохранения аккаунта с уже существующим в базе email
     */
    @Test
    void saveAccountWithExistsEmail() {
        String existedEmail = "email";
        Mockito.when(accountRepository.existsByEmail(existedEmail)).thenReturn(true);
        RegisterDTO dtoWithAlreadyExistsEmail = new RegisterDTO(
                "login", "password", existedEmail, "name"
        );
        AccountEmailAlreadyExists emailAlreadyExistsException = Assertions.assertThrows(
                AccountEmailAlreadyExists.class, () -> accountService.handleRegisterAccountRequest(dtoWithAlreadyExistsEmail)
        );
        Assert.assertEquals("account with email <email> already exists", emailAlreadyExistsException.getMessage());
        Mockito.verify(accountRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(messagingService, Mockito.never()).send(Mockito.any(), Mockito.any());
    }

    /**
     * Тест сохранения аккаунта с уже существующим в базе login
     */
    @Test
    void saveAccountWithExistsLogin() {
        String existedLogin = "login";
        Mockito.when(accountRepository.existsByLogin(existedLogin)).thenReturn(true);
        RegisterDTO dtoWithAlreadyExistsLogin = new RegisterDTO(
                existedLogin, "password", "email", "name"
        );
        AccountLoginAlreadyExists loginAlreadyExistsException = Assertions.assertThrows(
                AccountLoginAlreadyExists.class, () -> accountService.handleRegisterAccountRequest(dtoWithAlreadyExistsLogin)
        );
        Assert.assertEquals("account with login <login> already exists", loginAlreadyExistsException.getMessage());
        Mockito.verify(accountRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(messagingService, Mockito.never()).send(Mockito.any(), Mockito.any());
    }

}