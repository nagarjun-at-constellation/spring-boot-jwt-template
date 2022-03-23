package com.bancocaminos.impactbackendapi.twofa.security;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.bancocaminos.impactbackendapi.core.exception.authentication.UserInactiveException;
import com.bancocaminos.impactbackendapi.core.exception.authentication.VerificationCodeDoesNotMatchException;
import com.bancocaminos.impactbackendapi.core.exception.authentication.VerificationCodeNotFilledException;
import com.bancocaminos.impactbackendapi.core.exception.authentication.VerificationCodeNotGeneratedException;
import com.bancocaminos.impactbackendapi.master.usecase.MasterService;
import com.bancocaminos.impactbackendapi.master.usecase.entity.Master;
import com.bancocaminos.impactbackendapi.user.rest.model.UserModel;
import com.bancocaminos.impactbackendapi.user.usecase.IUserService;
import com.bancocaminos.impactbackendapi.user.usecase.entity.Users;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { CustomAuthenticationProvider.class })
public class CustomAuthenticationProviderTest {

    private static final String INVALID_VERIFICATION_CODE = "1235";
    private static final String VERIFICATION_CODE = "123456";
    private static final String MOCK_EMAIL_GMAIL_COM = "mock_email@gmail.com";

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @MockBean
    private IUserService userService;

    @MockBean
    private MasterService masterService;

    @Test
    public void emptyUserObject() {
        UserModel userModel = mock(UserModel.class);
        Master master = new Master();
        master.setId(1L);

        when(userModel.getVerificationCode()).thenReturn(VERIFICATION_CODE);
        when(userService.findByEmail(MOCK_EMAIL_GMAIL_COM)).thenReturn(Optional.empty());
        when(masterService.fetchMasterItem()).thenReturn(Optional.of(master));

        assertThrows(
                BadCredentialsException.class,
                () -> customAuthenticationProvider.validateVerificationCode(userModel),
                "Expected BadCredentialsException() to throw, but it didn't");
    }

    @Test
    public void emptyMasterObject() {
        UserModel userModel = mock(UserModel.class);

        Users user = new Users();
        user.setActive(true);
        when(userModel.getEmail()).thenReturn(MOCK_EMAIL_GMAIL_COM);

        when(userModel.getVerificationCode()).thenReturn(VERIFICATION_CODE);
        when(userService.findByEmail(MOCK_EMAIL_GMAIL_COM)).thenReturn(Optional.of(user));
        when(masterService.fetchMasterItem()).thenReturn(Optional.empty());

        assertThrows(
                BadCredentialsException.class,
                () -> customAuthenticationProvider.validateVerificationCode(userModel),
                "Expected BadCredentialsException() to throw, but it didn't");
    }

    @Test
    public void setMasterEnable2FAToFalse() {
        UserModel userModel = mock(UserModel.class);

        Users user = new Users();
        user.setUsing2FA(true);
        user.setActive(true);
        Master master = new Master();
        master.setEnable2FA(false);
        when(userModel.getPassword()).thenReturn("1234");

        when(userModel.getEmail()).thenReturn(MOCK_EMAIL_GMAIL_COM);

        when(userModel.getVerificationCode()).thenReturn(VERIFICATION_CODE);
        when(userService.findByEmail(MOCK_EMAIL_GMAIL_COM)).thenReturn(Optional.of(user));
        when(masterService.fetchMasterItem()).thenReturn(Optional.of(master));

        customAuthenticationProvider.validateVerificationCode(userModel);
    }

    @Test
    public void setUserIsUsing2FAToFalse() {
        UserModel userModel = mock(UserModel.class);

        Users user = new Users();
        user.setUsing2FA(false);
        user.setActive(true);
        Master master = new Master();
        master.setEnable2FA(true);
        when(userModel.getPassword()).thenReturn("1234");
        when(userModel.getEmail()).thenReturn(MOCK_EMAIL_GMAIL_COM);
        when(userModel.getVerificationCode()).thenReturn(VERIFICATION_CODE);
        when(userService.findByEmail(MOCK_EMAIL_GMAIL_COM)).thenReturn(Optional.of(user));
        when(masterService.fetchMasterItem()).thenReturn(Optional.of(master));

        customAuthenticationProvider.validateVerificationCode(userModel);
    }

    @Test
    public void emptySecret2FAKey() {
        UserModel userModel = mock(UserModel.class);

        Users user = new Users();
        user.setUsing2FA(true);
        user.setActive(true);
        Master master = new Master();
        master.setEnable2FA(true);
        when(userModel.getEmail()).thenReturn(MOCK_EMAIL_GMAIL_COM);

        when(userModel.getVerificationCode()).thenReturn(VERIFICATION_CODE);
        when(userService.findByEmail(MOCK_EMAIL_GMAIL_COM)).thenReturn(Optional.of(user));
        when(masterService.fetchMasterItem()).thenReturn(Optional.of(master));

        assertThrows(
                VerificationCodeNotGeneratedException.class,
                () -> customAuthenticationProvider.validateVerificationCode(userModel),
                "Expected VerificationCodeNotGeneratedException() to throw, but it didn't");
    }

    @Test
    public void verificationCodeNotFilled() {
        UserModel userModel = mock(UserModel.class);

        Users user = new Users();
        user.setUsing2FA(true);
        user.setActive(true);
        user.setSecret2FAKey(VERIFICATION_CODE);
        Master master = new Master();
        master.setEnable2FA(true);
        when(userModel.getEmail()).thenReturn(MOCK_EMAIL_GMAIL_COM);

        when(userModel.getVerificationCode()).thenReturn(null);
        when(userService.findByEmail(MOCK_EMAIL_GMAIL_COM)).thenReturn(Optional.of(user));
        when(masterService.fetchMasterItem()).thenReturn(Optional.of(master));

        assertThrows(
                VerificationCodeNotFilledException.class,
                () -> customAuthenticationProvider.validateVerificationCode(userModel),
                "Expected VerificationCodeNotFilledException() to throw, but it didn't");
    }

    @Test
    public void verificationCodeNotFilledEmpty() {
        UserModel userModel = mock(UserModel.class);

        Users user = new Users();
        user.setUsing2FA(true);
        user.setActive(true);
        user.setSecret2FAKey(VERIFICATION_CODE);
        Master master = new Master();
        master.setEnable2FA(true);
        when(userModel.getEmail()).thenReturn(MOCK_EMAIL_GMAIL_COM);

        when(userModel.getVerificationCode()).thenReturn("");
        when(userService.findByEmail(MOCK_EMAIL_GMAIL_COM)).thenReturn(Optional.of(user));
        when(masterService.fetchMasterItem()).thenReturn(Optional.of(master));

        assertThrows(
                VerificationCodeNotFilledException.class,
                () -> customAuthenticationProvider.validateVerificationCode(userModel),
                "Expected VerificationCodeNotFilledException() to throw, but it didn't");
    }

    @Test
    public void verificationCodeNotMatch() {
        UserModel userModel = mock(UserModel.class);

        Users user = new Users();
        user.setUsing2FA(true);
        user.setActive(true);
        user.setSecret2FAKey(INVALID_VERIFICATION_CODE);
        Master master = new Master();
        master.setEnable2FA(true);
        when(userModel.getEmail()).thenReturn(MOCK_EMAIL_GMAIL_COM);

        when(userModel.getVerificationCode()).thenReturn(VERIFICATION_CODE);
        when(userService.findByEmail(MOCK_EMAIL_GMAIL_COM)).thenReturn(Optional.of(user));
        when(masterService.fetchMasterItem()).thenReturn(Optional.of(master));

        assertThrows(
                VerificationCodeDoesNotMatchException.class,
                () -> customAuthenticationProvider.validateVerificationCode(userModel),
                "Expected VerificationCodeDoesNotMatchException() to throw, but it didn't");
    }

    @Test
    public void verificationCodeSuccessfully() {
        UserModel userModel = mock(UserModel.class);

        Users user = new Users();
        user.setUsing2FA(true);
        user.setActive(true);
        user.setSecret2FAKey(VERIFICATION_CODE);
        Master master = new Master();
        master.setEnable2FA(true);
        when(userModel.getEmail()).thenReturn(MOCK_EMAIL_GMAIL_COM);
        when(userModel.getVerificationCode()).thenReturn(VERIFICATION_CODE);
        when(userService.findByEmail(MOCK_EMAIL_GMAIL_COM)).thenReturn(Optional.of(user));
        when(masterService.fetchMasterItem()).thenReturn(Optional.of(master));
        when(userModel.getPassword()).thenReturn("1234");

        when(userModel.getEmail()).thenReturn(MOCK_EMAIL_GMAIL_COM);

        when(userModel.getVerificationCode()).thenReturn(VERIFICATION_CODE);

        customAuthenticationProvider.validateVerificationCode(userModel);
    }

    @Test
    public void inactiveUser() {
        UserModel userModel = mock(UserModel.class);

        Users user = new Users();
        user.setUsing2FA(true);
        user.setSecret2FAKey(VERIFICATION_CODE);
        user.setActive(false);
        Master master = new Master();

        master.setEnable2FA(true);
        when(userModel.getEmail()).thenReturn(MOCK_EMAIL_GMAIL_COM);
        when(userModel.getVerificationCode()).thenReturn(VERIFICATION_CODE);
        when(userService.findByEmail(MOCK_EMAIL_GMAIL_COM)).thenReturn(Optional.of(user));
        when(masterService.fetchMasterItem()).thenReturn(Optional.of(master));
        when(userModel.getPassword()).thenReturn("1234");
        when(userModel.getEmail()).thenReturn(MOCK_EMAIL_GMAIL_COM);
        when(userModel.getVerificationCode()).thenReturn(VERIFICATION_CODE);

        assertThrows(
                UserInactiveException.class,
                () -> customAuthenticationProvider.validateVerificationCode(userModel),
                "Expected UserInactiveException() to throw, but it didn't");
    }
}
