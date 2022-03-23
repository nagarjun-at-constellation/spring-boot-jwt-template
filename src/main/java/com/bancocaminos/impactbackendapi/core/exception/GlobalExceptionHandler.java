package com.bancocaminos.impactbackendapi.core.exception;

import com.bancocaminos.impactbackendapi.core.exception.authentication.UnableToAuthenticateUserException;
import com.bancocaminos.impactbackendapi.core.exception.authentication.UserAlreadyRegisteredException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Value(value = "${exception.userAlreadyRegisteredMessage}")
    private String userAlreadyRegisteredMessage;

    @Value(value = "${exception.unableToAuthenticateUserMessage}")
    private String unableToAuthenticateUserMessage;

    @ExceptionHandler(value = UnableToAuthenticateUserException.class)
    public ResponseEntity<String> unableToAuthenticateUserException(
            UnableToAuthenticateUserException unableToAuthenticateUserException) {
        return new ResponseEntity<>(unableToAuthenticateUserMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = UserAlreadyRegisteredException.class)
    public ResponseEntity<String> userAlreadyRegisteredException(
            UserAlreadyRegisteredException userAlreadyRegisteredException) {
        return new ResponseEntity<>(userAlreadyRegisteredMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
