package com.pw.ordermanager.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User authenticate(String userName, String password) {
        final User user = userRepository.findByUserName(userName);
        if(Optional.ofNullable(user).isPresent()){
            if(BCrypt.checkpw(password, user.getPassword())){
                return user;
            } else {
                User notAuth = new User();
                notAuth.setType(UserType.NOT_AUTH);
                return notAuth;
            }
        } else {
            User notFoundUser = new User();
            notFoundUser.setType(UserType.NONE);
            return notFoundUser;
        }
    }

}
