package com.bancocaminos.impactbackendapi.authentication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.bancocaminos.impactbackendapi.user.rest.model.Role;
import com.bancocaminos.impactbackendapi.user.usecase.IUserService;
import com.bancocaminos.impactbackendapi.user.usecase.entity.Users;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AuthenticationUserDetailService.class })
public class AuthenticationUserDetailServiceTest {

    private static final String PASSWORD = "1234";
    private static final String USERNAME = "abc@gmail.com";

    @MockBean
    private IUserService userService;

    @Autowired
    private AuthenticationUserDetailService authenticationUserDetailsService;

    @Test
    public void authenticationUserDetails() {
        Users user = new Users();
        user.setActive(true);
        user.setUsername(USERNAME);
        user.setRole(Role.ADMIN.name());
        user.setPassword(PASSWORD);

        when(userService.readUserByUsername(USERNAME)).thenReturn(user);

        UserDetails userDetails = authenticationUserDetailsService.loadUserByUsername(USERNAME);

        assertEquals(USERNAME, userDetails.getUsername());
        assertEquals(PASSWORD, userDetails.getPassword());
    }
}
