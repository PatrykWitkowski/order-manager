package com.pw.ordermanager.backend.utils.security;

import com.pw.ordermanager.backend.dts.UserDts;
import com.pw.ordermanager.backend.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@RunWith(SpringRunner.class)
public class SecurityUtilsTest {

    private static final String TOKEN = "3244-c34c-43v4-c42c";
    public static final String ADMIN_USERNAME = "admin";
    public static final String USER_NAME = "user";
    private UserDts user;

    @Before
    public void init(){
        user = new UserDts();
        user.setUser(User.builder().userName(ADMIN_USERNAME).build());
        user.setAuthorized(true);
        SecurityUtils.addAuthenticatedUser(TOKEN, user);
    }

    @Test
    public void shouldRevokeAccess(){
        SecurityUtils.revokeAccess(TOKEN);

        assertThat(SecurityUtils.isAccessGranted(TOKEN), is(false));
    }

    @Test
    public void shouldNoGainAccessWhenOtherUserLoged(){
        String otherSessionToken = "5345-df34-fd34-dr34";
        SecurityUtils.addAuthenticatedUser(otherSessionToken , user);

        final boolean result = SecurityUtils.isAccessGranted(otherSessionToken);

        assertThat(result, is(false));
    }

    @Test
    public void shouldNoGainAccessWhenUserNotAuthorized(){
        UserDts notAuthorizedUser = new UserDts();
        notAuthorizedUser.setUser(User.builder().userName(USER_NAME).build());
        notAuthorizedUser.setAuthorized(false);
        String otherSessionToken = "5345-df66-fd34-6gfd";
        SecurityUtils.addAuthenticatedUser(otherSessionToken, notAuthorizedUser);

        final boolean result = SecurityUtils.isAccessGranted(otherSessionToken);

        assertThat(result, is(false));
    }

    @Test
    public void shouldGainAccessWhenUserAuthorized(){
        final boolean result = SecurityUtils.isAccessGranted(TOKEN);

        assertThat(result, is(true));
    }
}
