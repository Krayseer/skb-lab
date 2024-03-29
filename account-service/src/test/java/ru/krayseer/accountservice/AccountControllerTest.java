package ru.krayseer.accountservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.krayseer.accountservice.domain.RegisterDTO;
import ru.krayseer.accountservice.exception.AccountEmailAlreadyExists;
import ru.krayseer.accountservice.exception.AccountLoginAlreadyExists;
import ru.krayseer.accountservice.service.AccountService;
import ru.krayseer.dto.AccountDTO;
import ru.krayseer.dto.ApiError;

import java.util.Map;

/**
 * Тесты для контроллера {@link AccountController}
 */
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    /**
     * Зарегистрировать аккаунт
     */
    @Test
    @SneakyThrows
    void registerAccount() {
        RegisterDTO registerDTO = new RegisterDTO(
                "login", "password", "email@gmail.com", "name"
        );
        AccountDTO expectedAccountDTO = new AccountDTO(1L, "login", "email@gmail.com", "name");
        Mockito.when(accountService.handleRegisterAccountRequest(registerDTO)).thenReturn(expectedAccountDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedAccountDTO)));
    }

    /**
     * Попытка зарегистрировать аккаунт с незаполненными данными (пустым {@link RegisterDTO}
     */
    @Test
    @SneakyThrows
    void registerAccountWithoutData() {
        RegisterDTO registerDTO = new RegisterDTO();
        ApiError<Map<String, String>> expectedContent = new ApiError<>(
                Map.of(
                        "login", "you need to enter login",
                        "password", "you need to enter password",
                        "email", "you need to enter email",
                        "name", "you need to enter name"
                )
        );
        mockMvc.perform(
                MockMvcRequestBuilders.post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedContent)));
    }

    /**
     * Попытка зарегистрировать аккаунт с некорректно введенным почтовым адресом
     */
    @Test
    @SneakyThrows
    void registerAccountWithInvalidEmail() {
        RegisterDTO registerDTO = new RegisterDTO(
                "login", "password", "incorrect_email", "name"
        );
        ApiError<Map<String, String>> expectedContent = new ApiError<>(Map.of("email", "bad email"));
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/account")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedContent)));
    }

    /**
     * Попытка зарегистрировать аккаунт уже с существующим email
     */
    @Test
    @SneakyThrows
    void registerAccountWithAlreadyExistsEmail() {
        RegisterDTO registerDTO = new RegisterDTO(
                "login", "password", "email@gmail.com", "name"
        );
        Mockito.when(accountService.handleRegisterAccountRequest(registerDTO))
                .thenThrow(new AccountEmailAlreadyExists("email@gmail.com"));
        ApiError<String> expectedContent = new ApiError<>("account with email <email@gmail.com> already exists");
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/account")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerDTO))
                )
                .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedContent)));
    }

    /**
     * Попытка зарегистрировать аккаунт уже с существующим login
     */
    @Test
    @SneakyThrows
    void registerAccountWithAlreadyExistsLogin() {
        RegisterDTO registerDTO = new RegisterDTO(
                "login", "password", "email@gmail.com", "name"
        );
        Mockito.when(accountService.handleRegisterAccountRequest(registerDTO))
                .thenThrow(new AccountLoginAlreadyExists("login"));
        ApiError<String> expectedContent = new ApiError<>("account with login <login> already exists");
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/account")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerDTO))
                )
                .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedContent)));
    }

}