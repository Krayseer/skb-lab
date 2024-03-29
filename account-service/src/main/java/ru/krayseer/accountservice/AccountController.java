package ru.krayseer.accountservice;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.krayseer.dto.AccountDTO;
import ru.krayseer.accountservice.domain.RegisterDTO;
import ru.krayseer.accountservice.service.AccountService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public AccountDTO registerAccount(@RequestBody @Valid RegisterDTO registerDTO) {
        return accountService.handleRegisterAccountRequest(registerDTO);
    }

}
