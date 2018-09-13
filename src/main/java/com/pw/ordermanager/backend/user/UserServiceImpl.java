package com.pw.ordermanager.backend.user;

import com.pw.ordermanager.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDts authenticate(String userName, String password) {
        final User user = userRepository.findByUserName(userName);
        UserDts userDts = new UserDts();
        userDts.setUser(user);

        Optional.ofNullable(user).ifPresent(u -> userDts.setAuthorized(BCrypt.checkpw(password, u.getPassword())));

        SecurityUtils.setCurrentUser(userDts);

        return userDts;
    }

}
