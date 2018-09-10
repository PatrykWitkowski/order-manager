package com.pw.ordermanager.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface UserRepository extends JpaRepository<User,Long>, Serializable {

    User findByUserName(String userName);
}
