package com.bancocaminos.impactbackendapi.core.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class VerificationCodeNotGeneratedException extends AuthenticationException {

	public VerificationCodeNotGeneratedException(String msg) {
		super(msg);
	}

}
