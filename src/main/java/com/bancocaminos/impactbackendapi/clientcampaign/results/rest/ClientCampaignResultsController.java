package com.bancocaminos.impactbackendapi.clientcampaign.results.rest;

import com.bancocaminos.impactbackendapi.clientcampaign.results.rest.model.ClientCampaignResultsRequestModel;
import com.bancocaminos.impactbackendapi.core.constants.GenericApiConstants;
import com.bancocaminos.impactbackendapi.user.rest.model.UserResponseModel;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = GenericApiConstants.ClientCampaignResult.CLIENT_CAMPAIGN_RESULTS_API)
@RequiredArgsConstructor
public class ClientCampaignResultsController {

    @PostMapping("/")
    public ResponseEntity<UserResponseModel> createUser(
            @RequestBody ClientCampaignResultsRequestModel clientCampaignResults) {
        UserResponseModel userResponse = new UserResponseModel();
        userResponse.setMessage("Successfully created the user");
        userResponse.setHttpStatus(HttpStatus.OK);
        return new ResponseEntity<>(userResponse, new HttpHeaders(), HttpStatus.OK);
    }
}
