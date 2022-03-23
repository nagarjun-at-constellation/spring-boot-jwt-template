package com.bancocaminos.impactbackendapi.user.usecase.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bancocaminos.impactbackendapi.user.rest.model.Role;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class UsersTest {

    private static final String PASSWORD = "password";
    private static final String EMAIL = "abc@gmail.com";
    private static final String PREFIX = "+34";
    private static final String MOBILE_NUMBER = "666666666";

    @Test
    public void userEntity() {
        Users user = new Users();
        user.setMobileNumber(MOBILE_NUMBER);
        user.setPrefix(PREFIX);
        user.setRole(Role.ADMIN.name());
        user.setUsername(EMAIL);
        user.setPassword(PASSWORD);
        user.setActive(true);

        assertEquals(MOBILE_NUMBER, user.getMobileNumber());
        assertEquals(PREFIX, user.getPrefix());
        assertEquals("ADMIN", user.getRole());
        assertEquals(EMAIL, user.getUsername());
        assertEquals(PASSWORD, user.getPassword());
        assertEquals(true, user.isActive());
    }
}
