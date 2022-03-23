package com.bancocaminos.impactbackendapi.core.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyRegisteredException extends AuthenticationException {

    public UserAlreadyRegisteredException() {
        super("");
    }
}
