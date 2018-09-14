package com.pw.ordermanager.backend.Dts;

import com.pw.ordermanager.backend.entity.User;
import lombok.Data;

@Data
public class UserDts {

    private User user;

    private boolean isAuthorized;
}
