package com.bancocaminos.impactbackendapi.twofa.controller;

import javax.validation.Valid;

import com.bancocaminos.impactbackendapi.twofa.model.SendSMSRequestModel;
import com.bancocaminos.impactbackendapi.twofa.model.SendSMSResponse;
import com.bancocaminos.impactbackendapi.twofa.model.SendSMSServiceResponse;
import com.bancocaminos.impactbackendapi.twofa.service.ISendSMSService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/second-factor")
public class SendSMSController {

	@Autowired
	private ISendSMSService sendSMSService;

	@PostMapping("/")
	public ResponseEntity<SendSMSResponse> updateSecurityKey(@Valid @RequestBody SendSMSRequestModel request) {
		HttpHeaders responseHeaders = new HttpHeaders();
		SendSMSResponse response = new SendSMSResponse();

		SendSMSServiceResponse smsServiceResponse = sendSMSService.createAndSendVerificationCode(request.getEmail(),
				request.getPassword());
		if (smsServiceResponse.isSmsSent()) {
			response.setStatus(HttpStatus.OK);
			response.setMessage("Successfully updated the security key and sent the SMS to the client");
			return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
		} else if (smsServiceResponse.isTwoFADeactivated()) {
			response.setStatus(HttpStatus.OK);
			response.setMessage("Two factor authentication is deactivated, Please proceed");
			return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
		}
		response.setStatus(HttpStatus.UNAUTHORIZED);
		response.setMessage("Unable to update the security key as the email entered is invalid");
		return new ResponseEntity<>(response, responseHeaders, HttpStatus.UNAUTHORIZED);
	}

}
