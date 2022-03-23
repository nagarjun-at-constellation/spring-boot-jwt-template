package com.bancocaminos.impactbackendapi.core.exceptions.authentication;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bancocaminos.impactbackendapi.core.exception.authentication.VerificationCodeNotFilledException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class VerificationCodeNotFilledExceptionTest {

	@Test
	public void successfulExceptionTest() {
		assertThrows(
				VerificationCodeNotFilledException.class,
				() -> throwError(),
				"Expected VerificationCodeNotFilledException() to throw, but it didn't");
	}

	private Object throwError() {
		throw new VerificationCodeNotFilledException("");
	}

	@Test
	public void superClassTest() {
		assertTrue((new VerificationCodeNotFilledException("")) instanceof AuthenticationException);
		assertTrue(AuthenticationException.class.isAssignableFrom(VerificationCodeNotFilledException.class));
	}
}
