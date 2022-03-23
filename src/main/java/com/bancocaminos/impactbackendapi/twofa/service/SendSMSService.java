package com.bancocaminos.impactbackendapi.twofa.service;

import java.util.Optional;
import java.util.Random;

import com.bancocaminos.impactbackendapi.authentication.service.ValidatePasswordService;
import com.bancocaminos.impactbackendapi.aws.sns.SNSService;
import com.bancocaminos.impactbackendapi.master.usecase.MasterService;
import com.bancocaminos.impactbackendapi.master.usecase.entity.Master;
import com.bancocaminos.impactbackendapi.twofa.model.SendSMSServiceResponse;
import com.bancocaminos.impactbackendapi.user.usecase.IUserService;
import com.bancocaminos.impactbackendapi.user.usecase.entity.Users;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
class SendSMSService implements ISendSMSService {

	private static Logger LOGGER = LogManager.getLogger(SendSMSService.class);
	private static Random rnd = new Random();

	@Autowired
	private IUserService userService;

	@Autowired
	private SNSService snsService;

	@Autowired
	private MasterService masterService;

	@Autowired
	private ValidatePasswordService validatePasswordService;

	@Value(value = "${sendSMS.message}")
	private String smsMessage;

	@Override
	public SendSMSServiceResponse createAndSendVerificationCode(final String email, final String providedPassword) {
		Optional<Users> user = userService.findByEmail(email);
		SendSMSServiceResponse sendSMSServiceResponse = new SendSMSServiceResponse();
		if (user.isEmpty()) {
			return sendSMSServiceResponse;
		}

		Optional<Master> master = masterService.fetchMasterItem();
		if (twoFADeactivated(user) || twoFAMasterDeactivated(master)) {
			sendSMSServiceResponse.setTwoFADeactivated(true);
			return sendSMSServiceResponse;
		}

		validatePasswordService.validatePassword(providedPassword, user.get().getPassword());

		generateAndProcess2FACode(user);
		sendSMSServiceResponse.setSmsSent(true);
		return sendSMSServiceResponse;
	}

	@Override
	public void generateAndProcess2FACode(Optional<Users> user) {
		int number = generateRandomSixDigits();
		String verificationMessage = new StringBuilder().append(smsMessage).append(number).toString();
		String mobileNumberWithPrefix = fetchMobileNumberWithPrefix(user);
		snsService.publishTextSMS(verificationMessage, mobileNumberWithPrefix);
		userService.updateSecretKey(number, user.get());
		LOGGER.info(
				new ParameterizedMessage("The SMS Code has been generated for the user {}", user.get().getUsername()));
	}

	private boolean twoFAMasterDeactivated(Optional<Master> master) {
		return master.isPresent() && !master.get().isEnable2FA();
	}

	private boolean twoFADeactivated(Optional<Users> user) {
		return user.isPresent() && !user.get().isUsing2FA();
	}

	private int generateRandomSixDigits() {
		return rnd.nextInt(999999);
	}

	public static String fetchMobileNumberWithPrefix(Optional<Users> user) {
		return new StringBuilder().append(user.get().getPrefix()).append(user.get().getMobileNumber()).toString();
	}
}
