package com.bancocaminos.impactbackendapi.twofa.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class SendSMSServiceResponseTest {

    @Test
    public void validateModel() {
        SendSMSServiceResponse sendSMSServiceResponse = new SendSMSServiceResponse();
        sendSMSServiceResponse.setInvalidPassword(true);
        sendSMSServiceResponse.setSmsSent(true);
        sendSMSServiceResponse.setTwoFADeactivated(true);
        assertTrue(sendSMSServiceResponse.isInvalidPassword());
        assertTrue(sendSMSServiceResponse.isSmsSent());
        assertTrue(sendSMSServiceResponse.isTwoFADeactivated());
    }

    @Test
    public void validateDefaultValues() {
        SendSMSServiceResponse sendSMSServiceResponse = new SendSMSServiceResponse();
        assertFalse(sendSMSServiceResponse.isInvalidPassword());
        assertFalse(sendSMSServiceResponse.isSmsSent());
        assertFalse(sendSMSServiceResponse.isTwoFADeactivated());
    }
}
