package com.pw.ordermanager.backend.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String userName;

    private String password;

    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
    }
}
