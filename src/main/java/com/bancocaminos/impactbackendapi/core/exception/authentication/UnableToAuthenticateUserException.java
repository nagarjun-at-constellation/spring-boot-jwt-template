package com.bancocaminos.impactbackendapi.core.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class UnableToAuthenticateUserException extends AuthenticationException {

    public UnableToAuthenticateUserException() {
        super("");
    }

}
