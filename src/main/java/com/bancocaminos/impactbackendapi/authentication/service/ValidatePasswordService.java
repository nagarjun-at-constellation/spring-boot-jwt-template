package com.bancocaminos.impactbackendapi.authentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ValidatePasswordService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean validatePassword(final String rawPassword, final String encodedPassword) {
        boolean userValidated = passwordEncoder.matches(rawPassword, encodedPassword);
        if (!userValidated) {
            throw new BadCredentialsException("The password provided is not valid");
        }
        return userValidated;
    }
}
