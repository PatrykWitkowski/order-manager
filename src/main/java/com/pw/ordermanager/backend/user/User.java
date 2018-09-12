package com.pw.ordermanager.backend.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String userName;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private UserType type;

    public User(String userName, String password, UserType type){
        this.userName = userName;
        this.password = password;
        this.type = type;
    }
}
