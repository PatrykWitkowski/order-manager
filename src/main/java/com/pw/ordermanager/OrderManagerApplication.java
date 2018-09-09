package com.pw.ordermanager;

import com.pw.ordermanager.backend.user.User;
import com.pw.ordermanager.backend.user.UserRepository;
import com.pw.ordermanager.backend.user.UserType;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
public class OrderManagerApplication {

    private static final Logger log = LoggerFactory.getLogger(OrderManagerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(OrderManagerApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(UserRepository repository) {
        return (args) -> {
            // temp
            repository.save(new User("admin", BCrypt.hashpw("admin", BCrypt.gensalt()), UserType.ADMIN));
        };
    }
}
