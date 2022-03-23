package com.bancocaminos.impactbackendapi.user.rest.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class UserResponseModelTest {

    private static final String MESSAGE = "message";

    @Test
    public void userResponseModelEntity() {
        UserResponseModel userResponseModel = new UserResponseModel();
        userResponseModel.setHttpStatus(HttpStatus.OK);
        userResponseModel.setMessage(MESSAGE);

        assertEquals(HttpStatus.OK, userResponseModel.getHttpStatus());
        assertEquals(MESSAGE, userResponseModel.getMessage());
    }
}
