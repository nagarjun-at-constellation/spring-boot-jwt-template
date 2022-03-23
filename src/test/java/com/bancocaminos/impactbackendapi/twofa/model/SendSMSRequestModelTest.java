package com.bancocaminos.impactbackendapi.twofa.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class SendSMSRequestModelTest {

    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";

    @Test
    public void validateModel() {
        SendSMSRequestModel sendSMSRequestModel = new SendSMSRequestModel();
        sendSMSRequestModel.setEmail(EMAIL);
        sendSMSRequestModel.setPassword(PASSWORD);
        assertEquals(EMAIL, sendSMSRequestModel.getEmail());
        assertEquals(PASSWORD, sendSMSRequestModel.getPassword());
    }
}
