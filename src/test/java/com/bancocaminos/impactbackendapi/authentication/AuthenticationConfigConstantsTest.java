package com.bancocaminos.impactbackendapi.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class AuthenticationConfigConstantsTest {

    @Test
    public void authenticationConstants() {
        long expirationTime = AuthenticationConfigConstants.EXPIRATION_TIME;
        String authroization = AuthenticationConfigConstants.HEADER_STRING;
        String secret = AuthenticationConfigConstants.SECRET;
        String bearer = AuthenticationConfigConstants.TOKEN_PREFIX;

        assertEquals(864000000, expirationTime);
        assertEquals("Authorization", authroization);
        assertEquals("Java_to_Dev_Secret", secret);
        assertEquals("Bearer ", bearer);
    }
}
