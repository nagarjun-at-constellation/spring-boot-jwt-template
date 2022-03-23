package com.bancocaminos.impactbackendapi.twofa.model;

import javax.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;

public class SendSMSResponse {

	@NotBlank
	private String message;

	@NotBlank
	private HttpStatus status;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

}
