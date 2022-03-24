package com.bancocaminos.impactbackendapi.user.rest.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserModel {

    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private Role role;

    @NotNull
    private String verificationCode;

    @NotNull
    private String mobileNumber;

    @NotNull
    private String prefix;
}
