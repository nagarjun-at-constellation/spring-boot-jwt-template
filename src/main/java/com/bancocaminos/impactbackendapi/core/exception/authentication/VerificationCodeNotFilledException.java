package com.bancocaminos.impactbackendapi.core.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class VerificationCodeNotFilledException extends AuthenticationException {

	public VerificationCodeNotFilledException(String msg) {
		super(msg);
	}

}
