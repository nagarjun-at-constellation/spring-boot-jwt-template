package com.bancocaminos.impactbackendapi.user.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bancocaminos.impactbackendapi.config.SecurityConfiguration;
import com.bancocaminos.impactbackendapi.core.constants.GenericApiConstants;
import com.bancocaminos.impactbackendapi.master.usecase.MasterService;
import com.bancocaminos.impactbackendapi.master.usecase.repository.MasterRepository;
import com.bancocaminos.impactbackendapi.twofa.controller.SendSMSController;
import com.bancocaminos.impactbackendapi.user.rest.model.Role;
import com.bancocaminos.impactbackendapi.user.rest.model.UserModel;
import com.bancocaminos.impactbackendapi.user.rest.model.UserResponseModel;
import com.bancocaminos.impactbackendapi.user.usecase.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(value = SendSMSController.class, excludeAutoConfiguration = { SecurityConfiguration.class,
        MasterService.class })
@ComponentScan(basePackages = "com.bancocaminos.impactbackendapi")
@TestPropertySource("classpath:test.properties")
public class UserRestControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MasterRepository masterRepository;

    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        setMvc(MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build());
    }

    @Test
    public void userRestController() throws Exception {
        UserModel usermodel = new UserModel();
        usermodel.setEmail("abc@gmail.com");
        usermodel.setMobileNumber("666666666");
        usermodel.setPassword("1234");
        usermodel.setPrefix("+34");
        usermodel.setRole(Role.ADMIN);
        usermodel.setVerificationCode("123456");

        RequestBuilder request = post(GenericApiConstants.User.USER_API + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(usermodel));
        MvcResult mvcResult = getMvc().perform(request).andExpect(status().isOk()).andReturn();
        String responseAsString = mvcResult.getResponse().getContentAsString();
        UserResponseModel requestModels = objectMapper.readValue(responseAsString,
                new TypeReference<UserResponseModel>() {
                });

        assertEquals(HttpStatus.OK, requestModels.getHttpStatus());
        assertEquals("Successfully created the user", requestModels.getMessage());
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

}
