package com.pw.ordermanager.backend.service;

import com.pw.ordermanager.backend.dts.UserDts;
import lombok.NonNull;

import java.io.Serializable;

public interface UserService extends Serializable {

    UserDts authenticate(@NonNull String userName, @NonNull String password, @NonNull String sessionToken);
}
