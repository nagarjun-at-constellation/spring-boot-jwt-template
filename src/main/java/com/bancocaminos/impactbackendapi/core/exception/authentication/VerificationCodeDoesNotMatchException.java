package com.bancocaminos.impactbackendapi.core.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class VerificationCodeDoesNotMatchException extends AuthenticationException {

	public VerificationCodeDoesNotMatchException(String msg) {
		super(msg);
	}

}
