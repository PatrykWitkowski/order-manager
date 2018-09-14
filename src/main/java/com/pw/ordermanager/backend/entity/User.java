package com.pw.ordermanager.backend.entity;

import com.pw.ordermanager.backend.common.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
