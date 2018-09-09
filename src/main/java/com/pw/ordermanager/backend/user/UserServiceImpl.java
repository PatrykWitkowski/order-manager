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
    public UserDts authenticate(String userName, String password) {
        final User user = userRepository.findByUserName(userName);
        UserDts userDts = new UserDts();
        userDts.setUser(user);

        if(Optional.ofNullable(user).isPresent()){
            if(BCrypt.checkpw(password, user.getPassword())){
                userDts.setAuthorized(true);
            } else {
                userDts.setAuthorized(false);
            }
        }
        return userDts;
    }

}
