package ru.krayseer.accountservice.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.krayseer.accountservice.exception.AccountLoginAlreadyExists;
import ru.krayseer.dto.AccountDTO;
import ru.krayseer.messaging.MessageQueue;
import ru.krayseer.messaging.Message;
import ru.krayseer.messaging.MessagingService;
import ru.krayseer.accountservice.AccountMapper;
import ru.krayseer.accountservice.domain.Account;
import ru.krayseer.accountservice.domain.RegisterDTO;
import ru.krayseer.accountservice.AccountRepository;
import ru.krayseer.accountservice.exception.AccountEmailAlreadyExists;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final MessagingService messagingService;

    @Override
    public AccountDTO handleRegisterAccountRequest(RegisterDTO registerDTO) {
        log.info("Start handle register request: {}", registerDTO);
        if (accountRepository.existsByLogin(registerDTO.getLogin())) {
            throw new AccountLoginAlreadyExists(registerDTO.getLogin());
        } else if (accountRepository.existsByEmail(registerDTO.getEmail())) {
            throw new AccountEmailAlreadyExists(registerDTO.getEmail());
        }
        Account account = accountRepository.save(AccountMapper.createFrom(registerDTO));
        AccountDTO accountDTO = AccountMapper.createTo(account);
        messagingService.send(MessageQueue.ACCOUNT_PROCESS_REQUEST, new Message<>(accountDTO));
        return accountDTO;
    }

}
