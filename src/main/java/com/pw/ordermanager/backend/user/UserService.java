package com.pw.ordermanager.backend.user;

import lombok.NonNull;

public interface UserService {

    UserDts authenticate(@NonNull String userName, @NonNull String password);
}
