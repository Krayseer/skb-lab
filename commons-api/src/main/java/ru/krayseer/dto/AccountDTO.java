package ru.krayseer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO с данными об аккаунте
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    /**
     * Идентификатор
     */
    private Long id;

    /**
     * Логин
     */
    private String login;

    /**
     * Почтовый адрес
     */
    private String email;

    /**
     * ФИО
     */
    private String name;

}
