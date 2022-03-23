package com.bancocaminos.impactbackendapi.twofa.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import com.bancocaminos.impactbackendapi.aws.sns.SNSService;
import com.bancocaminos.impactbackendapi.config.SecurityConfiguration;
import com.bancocaminos.impactbackendapi.core.constants.GenericApiConstants;
import com.bancocaminos.impactbackendapi.master.usecase.MasterService;
import com.bancocaminos.impactbackendapi.master.usecase.entity.Master;
import com.bancocaminos.impactbackendapi.twofa.model.SendSMSRequestModel;
import com.bancocaminos.impactbackendapi.user.usecase.IUserService;
import com.bancocaminos.impactbackendapi.user.usecase.entity.Users;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(value = SendSMSController.class, excludeAutoConfiguration = { SecurityConfiguration.class })
@ComponentScan(basePackages = "com.bancocaminos.impactbackendapi")
@TestPropertySource("classpath:test.properties")
public class SendSMSControllerTest {

    private static final String PREFIX = "+34";
    private static final String INVALID_EMAIL = "email";
    private static final String INVALID_RAW_PASSWORD = "12345";
    private static String EMAIL = "mockemail@gmail.com";
    private static final String MOBILE_NUMBER = "666666666";
    private static final String RAW_PASSWORD = "1234";
    private static String ENCODED_PASSWORD = "$2a$10$CLR7o8sZjVPFsY22BW6h0.V/IunHm92hO92YnhVe0Cc.XmX7uLPpq";

    @Autowired
    private WebApplicationContext context;

    @MockBean
    IUserService userService;

    @MockBean
    MasterService masterService;

    @MockBean
    SNSService snsService;

    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        setMvc(MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build());
    }

    @Test
    public void emptyEmailInRequest() throws Exception {
        SendSMSRequestModel requestContent = new SendSMSRequestModel();
        requestContent.setEmail("");
        requestContent.setPassword(RAW_PASSWORD);
        RequestBuilder request = post(GenericApiConstants.SECOND_FACTOR + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestContent));
        getMvc().perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void nullEmailInRequest() throws Exception {
        SendSMSRequestModel requestContent = new SendSMSRequestModel();
        requestContent.setPassword(RAW_PASSWORD);
        RequestBuilder request = post(GenericApiConstants.SECOND_FACTOR + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestContent));
        getMvc().perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void invalidEmailInRequest() throws Exception {
        SendSMSRequestModel requestContent = new SendSMSRequestModel();
        requestContent.setEmail(INVALID_EMAIL);
        requestContent.setPassword(RAW_PASSWORD);
        RequestBuilder request = post(GenericApiConstants.SECOND_FACTOR + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestContent));
        getMvc().perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void blankPasswordInRequest() throws Exception {
        SendSMSRequestModel requestContent = new SendSMSRequestModel();
        requestContent.setEmail(EMAIL);
        requestContent.setPassword("");
        RequestBuilder request = post(GenericApiConstants.SECOND_FACTOR + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestContent));
        getMvc().perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void emptyPasswordInRequest() throws Exception {
        SendSMSRequestModel requestContent = new SendSMSRequestModel();
        requestContent.setEmail(EMAIL);
        requestContent.setPassword(null);
        RequestBuilder request = post(GenericApiConstants.SECOND_FACTOR + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestContent));
        getMvc().perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void successfulSMSSentResponse() throws Exception {
        SendSMSRequestModel requestContent = new SendSMSRequestModel();
        requestContent.setEmail(EMAIL);
        requestContent.setPassword(RAW_PASSWORD);
        RequestBuilder request = post(GenericApiConstants.SECOND_FACTOR + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestContent));

        Users users = new Users();
        users.setActive(true);
        users.setUsing2FA(true);
        users.setMobileNumber(MOBILE_NUMBER);
        users.setPrefix(PREFIX);
        users.setPassword(ENCODED_PASSWORD);
        Mockito.when(getUserService().findByEmail(EMAIL)).thenReturn(Optional.of(users));
        Master master = new Master();
        master.setId(1L);
        master.setEnable2FA(true);
        Mockito.when(getMasterService().fetchMasterItem()).thenReturn(Optional.of(master));

        getMvc().perform(request).andExpect(status().isOk());
        verify(getUserService(), times(1)).findByEmail(EMAIL);
        verify(getMasterService(), times(1)).fetchMasterItem();
        verify(getUserService(), times(1)).updateSecretKey(anyInt(), any());
        verify(getSnsService(), times(1)).publishTextSMS(anyString(), anyString());
    }

    @Test
    public void emptyUserFailure() throws Exception {
        SendSMSRequestModel requestContent = new SendSMSRequestModel();
        requestContent.setEmail(EMAIL);
        requestContent.setPassword(RAW_PASSWORD);
        RequestBuilder request = post(GenericApiConstants.SECOND_FACTOR + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestContent));

        Mockito.when(getUserService().findByEmail(EMAIL)).thenReturn(Optional.empty());
        Master master = new Master();
        master.setId(1L);
        master.setEnable2FA(true);
        Mockito.when(getMasterService().fetchMasterItem()).thenReturn(Optional.of(master));

        getMvc().perform(request).andExpect(status().isUnauthorized());
        verify(getUserService(), times(1)).findByEmail(EMAIL);
        verify(getUserService(), times(0)).updateSecretKey(anyInt(), any());
        verify(getSnsService(), times(0)).publishTextSMS(anyString(), anyString());
    }

    @Test
    public void master2FADeactivated() throws Exception {
        SendSMSRequestModel requestContent = new SendSMSRequestModel();
        requestContent.setEmail(EMAIL);
        requestContent.setPassword(RAW_PASSWORD);
        RequestBuilder request = post(GenericApiConstants.SECOND_FACTOR + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestContent));

        Users users = new Users();
        users.setActive(true);
        users.setUsing2FA(true);
        users.setMobileNumber(MOBILE_NUMBER);
        users.setPrefix(PREFIX);
        users.setPassword(ENCODED_PASSWORD);
        Mockito.when(getUserService().findByEmail(EMAIL)).thenReturn(Optional.of(users));
        Master master = new Master();
        master.setId(1L);
        master.setEnable2FA(false);
        Mockito.when(getMasterService().fetchMasterItem()).thenReturn(Optional.of(master));

        getMvc().perform(request).andExpect(status().isContinue());
        verify(getUserService(), times(1)).findByEmail(EMAIL);
        verify(getMasterService(), times(1)).fetchMasterItem();
        verify(getUserService(), times(0)).updateSecretKey(anyInt(), any());
        verify(getSnsService(), times(0)).publishTextSMS(anyString(), anyString());
    }

    @Test
    public void user2FADeactivated() throws Exception {
        SendSMSRequestModel requestContent = new SendSMSRequestModel();
        requestContent.setEmail(EMAIL);
        requestContent.setPassword(RAW_PASSWORD);
        RequestBuilder request = post(GenericApiConstants.SECOND_FACTOR + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestContent));

        Users users = new Users();
        users.setActive(true);
        users.setUsing2FA(false);
        users.setMobileNumber(MOBILE_NUMBER);
        users.setPrefix(PREFIX);
        users.setPassword(ENCODED_PASSWORD);
        Mockito.when(getUserService().findByEmail(EMAIL)).thenReturn(Optional.of(users));
        Master master = new Master();
        master.setId(1L);
        master.setEnable2FA(true);
        Mockito.when(getMasterService().fetchMasterItem()).thenReturn(Optional.of(master));

        getMvc().perform(request).andExpect(status().isContinue());
        verify(getUserService(), times(1)).findByEmail(EMAIL);
        verify(getMasterService(), times(1)).fetchMasterItem();
        verify(getUserService(), times(0)).updateSecretKey(anyInt(), any());
        verify(getSnsService(), times(0)).publishTextSMS(anyString(), anyString());
    }

    @Test
    public void invalidPassword() throws Exception {
        SendSMSRequestModel requestContent = new SendSMSRequestModel();
        requestContent.setEmail(EMAIL);
        requestContent.setPassword(INVALID_RAW_PASSWORD);
        RequestBuilder request = post(GenericApiConstants.SECOND_FACTOR + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestContent));

        Users users = new Users();
        users.setActive(true);
        users.setUsing2FA(true);
        users.setMobileNumber(MOBILE_NUMBER);
        users.setPrefix(PREFIX);
        users.setPassword(ENCODED_PASSWORD);
        Mockito.when(getUserService().findByEmail(EMAIL)).thenReturn(Optional.of(users));
        Master master = new Master();
        master.setId(1L);
        master.setEnable2FA(true);
        Mockito.when(getMasterService().fetchMasterItem()).thenReturn(Optional.of(master));

        getMvc().perform(request).andExpect(status().isForbidden());
        verify(getUserService(), times(1)).findByEmail(EMAIL);
        verify(getMasterService(), times(1)).fetchMasterItem();
        verify(getUserService(), times(0)).updateSecretKey(anyInt(), any());
        verify(getSnsService(), times(0)).publishTextSMS(anyString(), anyString());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MockMvc getMvc() {
        return this.mvc;
    }

    public void setMvc(MockMvc mvc) {
        this.mvc = mvc;
    }

    public WebApplicationContext getContext() {
        return this.context;
    }

    public void setContext(WebApplicationContext context) {
        this.context = context;
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
