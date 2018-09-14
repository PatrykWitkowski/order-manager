package com.pw.ordermanager.backend.jpa;

import com.pw.ordermanager.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface UserRepository extends JpaRepository<User,Long>, Serializable {

    User findByUserName(String userName);
}
