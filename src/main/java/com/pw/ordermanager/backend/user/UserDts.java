package com.pw.ordermanager.backend.user;

import lombok.Data;

@Data
public class UserDts {

    private User user;

    private boolean isAuthorized;
}
