package com.bancocaminos.impactbackendapi.authentication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ValidatePasswordService.class })
public class ValidatePasswordServiceTest {

    private static final String RAW_PASSWORD = "1234";
    private static String ENCODED_PASSWORD = "$2a$10$CLR7o8sZjVPFsY22BW6h0.V/IunHm92hO92YnhVe0Cc.XmX7uLPpq";

    @Autowired
    private ValidatePasswordService validatePasswordService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void validatePassword() {
        when(passwordEncoder.matches(RAW_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);

        boolean validatedPassword = validatePasswordService.validatePassword(RAW_PASSWORD, ENCODED_PASSWORD);

        assertEquals(true, validatedPassword);
    }

    @Test
    public void validatePasswordThrowError() {
        when(passwordEncoder.matches(RAW_PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

        assertThrows(
                BadCredentialsException.class,
                () -> validatePasswordService.validatePassword(RAW_PASSWORD, ENCODED_PASSWORD),
                "Expected BadCredentialsException() to throw, but it didn't");
    }

}
