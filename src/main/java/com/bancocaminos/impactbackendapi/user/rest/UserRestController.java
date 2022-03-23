package com.bancocaminos.impactbackendapi.user.rest;

import com.bancocaminos.impactbackendapi.core.constants.GenericApiConstants;
import com.bancocaminos.impactbackendapi.user.rest.model.UserModel;
import com.bancocaminos.impactbackendapi.user.rest.model.UserResponseModel;
import com.bancocaminos.impactbackendapi.user.usecase.IUserService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = GenericApiConstants.User.USER_API)
@RequiredArgsConstructor
public class UserRestController {
    private final IUserService userService;

    @PostMapping("/")
    public ResponseEntity<UserResponseModel> createUser(@RequestBody UserModel userModel) {
        userService.createUser(userModel);
        UserResponseModel userResponse = new UserResponseModel();
        userResponse.setMessage("Successfully created the user");
        userResponse.setHttpStatus(HttpStatus.OK);
        return new ResponseEntity<>(userResponse, new HttpHeaders(), HttpStatus.OK);
    }

}
