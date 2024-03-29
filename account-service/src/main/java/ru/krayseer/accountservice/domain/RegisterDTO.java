package ru.krayseer.accountservice.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO с регистрационными данными аккаунта
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {

    /**
     * Логин
     */
    @NotBlank(message = "you need to enter login")
    private String login;

    /**
     * Пароль
     */
    @NotBlank(message = "you need to enter password")
    private String password;

    /**
     * Почтовый адрес
     */
    @Email(message = "bad email")
    @NotBlank(message = "you need to enter email")
    private String email;

    /**
     * ФИО
     */
    @NotBlank(message = "you need to enter name")
    private String name;

}
