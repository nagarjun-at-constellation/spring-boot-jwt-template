package com.bancocaminos.impactbackendapi.core.exceptions.authentication;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bancocaminos.impactbackendapi.core.exception.authentication.VerificationCodeDoesNotMatchException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class VerificationCodeDoesNotMatchExceptionTest {

    @Test
    public void successfulExceptionTest() {
        assertThrows(
                VerificationCodeDoesNotMatchException.class,
                () -> throwError(),
                "Expected VerificationCodeDoesNotMatchException() to throw, but it didn't");
    }

    private Object throwError() {
        throw new VerificationCodeDoesNotMatchException("");
    }

    @Test
    public void superClassTest() {
        assertTrue((new VerificationCodeDoesNotMatchException("")) instanceof AuthenticationException);
        assertTrue(AuthenticationException.class.isAssignableFrom(VerificationCodeDoesNotMatchException.class));
    }
}
