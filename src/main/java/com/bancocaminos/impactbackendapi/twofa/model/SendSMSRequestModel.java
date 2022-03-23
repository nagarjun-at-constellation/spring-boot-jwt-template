package com.bancocaminos.impactbackendapi.twofa.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class SendSMSRequestModel {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
