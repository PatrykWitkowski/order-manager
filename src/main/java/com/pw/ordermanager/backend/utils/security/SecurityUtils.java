package com.pw.ordermanager.backend.utils.security;

import com.pw.ordermanager.backend.Dts.UserDts;

import java.util.Optional;

public class SecurityUtils {

    static private UserDts currentUser;

    public static boolean isAccessGranted(){
        return Optional.ofNullable(currentUser).map(UserDts::isAuthorized).orElse(false);
    }

    public static void setCurrentUser(UserDts currentUser) {
        SecurityUtils.currentUser = currentUser;
    }

    public static void revokeAccess(){
        SecurityUtils.setCurrentUser(null);
    }
}
