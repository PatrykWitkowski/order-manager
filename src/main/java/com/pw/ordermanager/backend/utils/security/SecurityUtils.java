package com.pw.ordermanager.backend.utils.security;

import com.pw.ordermanager.backend.dts.UserDts;

import java.util.Optional;

public class SecurityUtils {

    private static UserDts currentUser;

    private SecurityUtils() {
        throw new IllegalStateException("Utility class");
    }

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
