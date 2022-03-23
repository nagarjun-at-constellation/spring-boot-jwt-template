package com.bancocaminos.impactbackendapi.user.rest.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class UserModelTest {

    private static final String PREFIX = "+34";
    private static final String PASSWORD = "1234";
    private static final String MOBILE_NUMBER = "666666666";
    private static final String EMAIL = "abc@gmail.com";

    @Test
    public void userModelTest() {
        UserModel usermodel = new UserModel();
        usermodel.setEmail(EMAIL);
        usermodel.setMobileNumber(MOBILE_NUMBER);
        usermodel.setPassword(PASSWORD);
        usermodel.setPrefix(PREFIX);
        usermodel.setRole(Role.ADMIN);

        assertEquals(EMAIL, usermodel.getEmail());
        assertEquals(PASSWORD, usermodel.getPassword());
        assertEquals(PREFIX, usermodel.getPrefix());
        assertEquals(MOBILE_NUMBER, usermodel.getMobileNumber());
        assertEquals(Role.ADMIN, usermodel.getRole());
    }
}
