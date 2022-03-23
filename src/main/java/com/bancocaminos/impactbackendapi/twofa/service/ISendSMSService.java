package com.bancocaminos.impactbackendapi.twofa.service;

import java.util.Optional;

import com.bancocaminos.impactbackendapi.twofa.model.SendSMSServiceResponse;
import com.bancocaminos.impactbackendapi.user.usecase.entity.Users;

public interface ISendSMSService {
    public SendSMSServiceResponse createAndSendVerificationCode(final String email, final String providedPassword);

    public void generateAndProcess2FACode(Optional<Users> user);
}
