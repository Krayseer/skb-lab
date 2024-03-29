package ru.krayseer.accountservice.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Аккаунт
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Логин
     */
    @Column(name = "login")
    private String login;

    /**
     * Пароль
     */
    @Column(name = "password")
    private String password;

    /**
     * Почтовый адрес
     */
    @Column(name = "email")
    private String email;

    /**
     * ФИО
     */
    @Column(name = "name")
    private String name;

}
