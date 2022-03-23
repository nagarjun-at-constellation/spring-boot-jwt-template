package com.bancocaminos.impactbackendapi.core.exceptions.authentication;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bancocaminos.impactbackendapi.core.exception.authentication.VerificationCodeNotGeneratedException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class VerificationCodeNotGeneratedExceptionTest {

	@Test
	public void successfulExceptionTest() {
		assertThrows(
				VerificationCodeNotGeneratedException.class,
				() -> throwError(),
				"Expected VerificationCodeNotFilledException() to throw, but it didn't");
	}

	private Object throwError() {
		throw new VerificationCodeNotGeneratedException("");
	}

	@Test
	public void superClassTest() {
		assertTrue((new VerificationCodeNotGeneratedException("")) instanceof AuthenticationException);
		assertTrue(AuthenticationException.class.isAssignableFrom(VerificationCodeNotGeneratedException.class));
	}
}
