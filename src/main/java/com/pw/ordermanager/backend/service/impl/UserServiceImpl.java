package com.pw.ordermanager.backend.service.impl;

import com.pw.ordermanager.backend.dts.UserDts;
import com.pw.ordermanager.backend.entity.User;
import com.pw.ordermanager.backend.jpa.UserRepository;
import com.pw.ordermanager.backend.service.UserService;
import com.pw.ordermanager.backend.utils.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDts authenticate(String userName, String password, String sessionToken) {
        final User user = userRepository.findByUsername(userName);
        UserDts userDts = new UserDts();
        userDts.setUser(user);

        Optional.ofNullable(user).ifPresent(u -> userDts.setAuthorized(BCrypt.checkpw(password, u.getPassword())));
        SecurityUtils.addAuthenticatedUser(sessionToken, userDts);

        return userDts;
    }

}
