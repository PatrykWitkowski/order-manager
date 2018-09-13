package com.pw.ordermanager.backend.user;

import com.pw.ordermanager.security.SecurityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService testedService;

    @MockBean
    private UserRepository userRepositoryMock;

    private User admin;

    @Before
    public void init(){
        when(userRepositoryMock.findByUserName("unknow")).thenReturn(null);
        final String correctPass = BCrypt.hashpw("correct", BCrypt.gensalt());
        admin = User.builder().userName("admin").password(correctPass).type(UserType.ADMIN).build();
        when(userRepositoryMock.findByUserName("admin")).thenReturn(admin);
    }

    @Test
    public void shouldNotAuthenticateWhenNoFoundUser(){
        final UserDts result = testedService.authenticate("unknow", "unknow");

        assertThat(result.getUser(), is(nullValue()));
        assertThat(result.isAuthorized(), is(false));
        assertThat(SecurityUtils.isAccessGranted(), is(false));
    }

    @Test
    public void shouldNotAuthenticateWhenBadPassword(){
        final UserDts result = testedService.authenticate("admin", "wrong");

        assertThat(result.getUser(), is(admin));
        assertThat(result.isAuthorized(), is(false));
        assertThat(SecurityUtils.isAccessGranted(), is(false));
    }

    @Test
    public void shouldAuthenticateWhenCorrectPassword(){
        final UserDts result = testedService.authenticate("admin", "correct");

        assertThat(result.getUser(), is(admin));
        assertThat(result.isAuthorized(), is(true));
        assertThat(SecurityUtils.isAccessGranted(), is(true));
    }
}
