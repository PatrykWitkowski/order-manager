package com.pw.ordermanager.backend.utils.security;

import com.pw.ordermanager.backend.dts.UserDts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@RunWith(SpringRunner.class)
public class SecurityUtilsTest {

    @Before
    public void init(){
        SecurityUtils.setCurrentUser(null);
    }

    @Test
    public void shouldRevokeAccess(){
        SecurityUtils.revokeAccess();

        assertThat(SecurityUtils.isAccessGranted(), is(false));
    }

    @Test
    public void shouldNoGainAccessWhenNoCurrentUser(){
        final boolean result = SecurityUtils.isAccessGranted();

        assertThat(result, is(false));
    }

    @Test
    public void shouldNoGainAccessWhenCurrentUserNoAuthorized(){
        UserDts currentUser = new UserDts();
        currentUser.setAuthorized(false);
        SecurityUtils.setCurrentUser(currentUser);

        final boolean result = SecurityUtils.isAccessGranted();

        assertThat(result, is(false));
    }

    @Test
    public void shouldGainAccessWhenCurrentUserAuthorized(){
        UserDts currentUser = new UserDts();
        currentUser.setAuthorized(true);
        SecurityUtils.setCurrentUser(currentUser);

        final boolean result = SecurityUtils.isAccessGranted();

        assertThat(result, is(true));
    }
}
