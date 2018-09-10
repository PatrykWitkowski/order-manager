package com.pw.ordermanager.backend.user;

import lombok.NonNull;

import java.io.Serializable;

public interface UserService extends Serializable {

    UserDts authenticate(@NonNull String userName, @NonNull String password);
}
