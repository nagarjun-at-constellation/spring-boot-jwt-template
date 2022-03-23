package com.bancocaminos.impactbackendapi.user.rest.model;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class UserResponseModel {
    private String message;
    private HttpStatus httpStatus;
}
