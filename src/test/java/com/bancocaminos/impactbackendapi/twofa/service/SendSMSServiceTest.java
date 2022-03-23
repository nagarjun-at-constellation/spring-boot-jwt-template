package com.bancocaminos.impactbackendapi.twofa.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.bancocaminos.impactbackendapi.authentication.service.ValidatePasswordService;
import com.bancocaminos.impactbackendapi.aws.sns.SNSService;
import com.bancocaminos.impactbackendapi.core.exception.aws.UnableToPublishMessageException;
import com.bancocaminos.impactbackendapi.master.usecase.MasterService;
import com.bancocaminos.impactbackendapi.master.usecase.entity.Master;
import com.bancocaminos.impactbackendapi.twofa.model.SendSMSServiceResponse;
import com.bancocaminos.impactbackendapi.user.usecase.IUserService;
import com.bancocaminos.impactbackendapi.user.usecase.entity.Users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SendSMSService.class })
public class SendSMSServiceTest {

    private static final String PREFIX = "+34";
    private static final String RAW_PASSWORD = "1234";
    private static final String MOBILE_NUMBER = "666666666";
    private static String EMAIL = "mockemail@gmail.com";
    private static String ENCODED_PASSWORD = "$2a$10$CLR7o8sZjVPFsY22BW6h0.V/IunHm92hO92YnhVe0Cc.XmX7uLPpq";

    @Autowired
    private ISendSMSService sendSMSService;

    @MockBean
    private IUserService userService;

    @MockBean
    private MasterService masterService;

    @MockBean
    private SNSService snsService;

    @MockBean
    private ValidatePasswordService validatePasswordService;

    @BeforeEach
    public void setup() {
        when(validatePasswordService.validatePassword(RAW_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
    }

    @Test
    public void sendVerificationCodeSuccessfully() {
        Users user = new Users();
        user.setActive(true);
        user.setUsing2FA(true);
        user.setMobileNumber(MOBILE_NUMBER);
        user.setPrefix(PREFIX);
        user.setPassword(ENCODED_PASSWORD);
        Mockito.when(getUserService().findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Master master = new Master();
        master.setId(1L);
        master.setEnable2FA(true);
        Mockito.when(getMasterService().fetchMasterItem()).thenReturn(Optional.of(master));

        SendSMSServiceResponse sendSMSServiceResponse = sendSMSService.createAndSendVerificationCode(EMAIL,
                RAW_PASSWORD);
        assertTrue(sendSMSServiceResponse.isSmsSent());
        assertFalse(sendSMSServiceResponse.isTwoFADeactivated());
        verify(getUserService(), times(1)).findByEmail(EMAIL);
        verify(getMasterService(), times(1)).fetchMasterItem();
        verify(getUserService(), times(1)).updateSecretKey(anyInt(), any());
        verify(getSnsService(), times(1)).publishTextSMS(anyString(), anyString());
    }

    @Test
    public void unableToFindUser() {
        Mockito.when(getUserService().findByEmail(EMAIL)).thenReturn(Optional.empty());

        SendSMSServiceResponse sendSMSServiceResponse = sendSMSService.createAndSendVerificationCode(EMAIL,
                RAW_PASSWORD);
        assertFalse(sendSMSServiceResponse.isSmsSent());
        assertFalse(sendSMSServiceResponse.isTwoFADeactivated());
        verify(getUserService(), times(1)).findByEmail(EMAIL);
        verify(getMasterService(), times(0)).fetchMasterItem();
        verify(getUserService(), times(0)).updateSecretKey(anyInt(), any());
        verify(getSnsService(), times(0)).publishTextSMS(anyString(), anyString());
    }

    @Test
    public void twoFADeactivated() {
        Users user = new Users();
        user.setActive(true);
        user.setUsing2FA(false);
        user.setMobileNumber(MOBILE_NUMBER);
        user.setPrefix(PREFIX);
        Mockito.when(getUserService().findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Master master = new Master();
        master.setId(1L);
        master.setEnable2FA(true);
        Mockito.when(getMasterService().fetchMasterItem()).thenReturn(Optional.of(master));

        SendSMSServiceResponse sendSMSServiceResponse = sendSMSService.createAndSendVerificationCode(EMAIL,
                RAW_PASSWORD);
        assertFalse(sendSMSServiceResponse.isSmsSent());
        assertTrue(sendSMSServiceResponse.isTwoFADeactivated());
        verify(getMasterService(), times(1)).fetchMasterItem();
        verify(getUserService(), times(1)).findByEmail(EMAIL);
        verify(getUserService(), times(0)).updateSecretKey(anyInt(), any());
        verify(getSnsService(), times(0)).publishTextSMS(anyString(), anyString());
    }

    @Test
    public void masterTwoFADeactivated() {
        Users user = new Users();
        user.setActive(true);
        user.setUsing2FA(true);
        user.setMobileNumber(MOBILE_NUMBER);
        user.setPrefix(PREFIX);
        Mockito.when(getUserService().findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Master master = new Master();
        master.setId(1L);
        master.setEnable2FA(false);
        Mockito.when(getMasterService().fetchMasterItem()).thenReturn(Optional.of(master));

        SendSMSServiceResponse sendSMSServiceResponse = sendSMSService.createAndSendVerificationCode(EMAIL,
                RAW_PASSWORD);
        assertFalse(sendSMSServiceResponse.isSmsSent());
        assertTrue(sendSMSServiceResponse.isTwoFADeactivated());
        verify(getUserService(), times(1)).findByEmail(EMAIL);
        verify(getMasterService(), times(1)).fetchMasterItem();
        verify(getUserService(), times(0)).updateSecretKey(anyInt(), any());
        verify(getSnsService(), times(0)).publishTextSMS(anyString(), anyString());
    }

    @Test
    public void unableToPublishMessageException() {
        Users user = new Users();
        user.setActive(true);
        user.setUsing2FA(true);
        user.setMobileNumber(MOBILE_NUMBER);
        user.setPrefix(PREFIX);
        user.setPassword(ENCODED_PASSWORD);
        Mockito.when(getUserService().findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Master master = new Master();
        master.setId(1L);
        master.setEnable2FA(true);
        Mockito.when(getMasterService().fetchMasterItem()).thenReturn(Optional.of(master));
        doThrow(UnableToPublishMessageException.class).when(getSnsService()).publishTextSMS(anyString(), anyString());

        assertThrows(
                UnableToPublishMessageException.class,
                () -> sendSMSService.createAndSendVerificationCode(EMAIL,
                        RAW_PASSWORD),
                "Expected UnableToPublishMessageException() to throw, but it didn't");

        verify(getMasterService(), times(1)).fetchMasterItem();
        verify(getUserService(), times(1)).findByEmail(EMAIL);
        verify(getUserService(), times(0)).updateSecretKey(anyInt(), any());
        verify(getSnsService(), times(1)).publishTextSMS(anyString(), anyString());
    }

    @Test
    public void generateAndProcess2FA() {
        Users user = new Users();
        user.setMobileNumber(MOBILE_NUMBER);
        user.setPrefix(PREFIX);
        sendSMSService.generateAndProcess2FACode(Optional.of(user));
        verify(getUserService(), times(1)).updateSecretKey(anyInt(), any());
        verify(getSnsService(), times(1)).publishTextSMS(anyString(), anyString());
    }

    public ISendSMSService getSendSMSService() {
        return this.sendSMSService;
    }

    public void setSendSMSService(SendSMSService sendSMSService) {
        this.sendSMSService = sendSMSService;
    }

    public IUserService getUserService() {
        return this.userService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public SNSService getSnsService() {
        return this.snsService;
    }

    public void setSnsService(SNSService snsService) {
        this.snsService = snsService;
    }

    public MasterService getMasterService() {
        return this.masterService;
    }

    public void setMasterService(MasterService masterService) {
        this.masterService = masterService;
    }

}
