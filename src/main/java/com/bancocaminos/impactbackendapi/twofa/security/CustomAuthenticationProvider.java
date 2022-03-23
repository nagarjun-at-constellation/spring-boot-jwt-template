package com.bancocaminos.impactbackendapi.twofa.security;

import java.util.Optional;

import com.bancocaminos.impactbackendapi.core.exception.authentication.UserInactiveException;
import com.bancocaminos.impactbackendapi.core.exception.authentication.VerificationCodeDoesNotMatchException;
import com.bancocaminos.impactbackendapi.core.exception.authentication.VerificationCodeNotFilledException;
import com.bancocaminos.impactbackendapi.core.exception.authentication.VerificationCodeNotGeneratedException;
import com.bancocaminos.impactbackendapi.core.utils.HydraStringUtils;
import com.bancocaminos.impactbackendapi.core.utils.ImpactNumberUtils;
import com.bancocaminos.impactbackendapi.master.usecase.IMasterService;
import com.bancocaminos.impactbackendapi.master.usecase.entity.Master;
import com.bancocaminos.impactbackendapi.user.rest.model.UserModel;
import com.bancocaminos.impactbackendapi.user.usecase.IUserService;
import com.bancocaminos.impactbackendapi.user.usecase.entity.Users;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider {

	private static final String THE_USER_IS_INACTIVE = "El usuario no está activo";
	private static final String INVALID_USERNAME_OR_PASSWORD = "Users o Contraseña no son correctos. Reviselo por favor.";
	private static Logger LOGGER = LogManager.getLogger(CustomAuthenticationProvider.class);

	@Value(value = "${error.authentication.verificationCodeNotGeneratedMessage}")
	private String verificationCodeNotGeneratedMessage;

	@Value(value = "${error.authentication.verificationCodeNotFilledMessage}")
	private String verificationCodeNotFilledMessage;

	@Value(value = "${error.authentication.verificationCodeDoesNotMatchMessage}")
	private String verificationCodeDoesNotMatchMessage;

	@Autowired
	private IUserService userService;

	@Autowired
	private IMasterService masterService;

	public void validateVerificationCode(UserModel userModel) throws AuthenticationException {
		LOGGER.info("Custom authentication provider is actived");
		final Optional<Users> user = userService.findByEmail(userModel.getEmail());
		final Optional<Master> master = masterService.fetchMasterItem();
		final String verificationCode = userModel.getVerificationCode();

		if (user.isEmpty() || master.isEmpty()) {
			throw new BadCredentialsException(INVALID_USERNAME_OR_PASSWORD);
		}

		if (isUserInactive(user.get())) {
			throw new UserInactiveException(THE_USER_IS_INACTIVE);
		}

		final String validVerificationCode = user.get().getSecret2FAKey();
		LOGGER.info(new ParameterizedMessage("The two factor authentication is {} for user {}", user.get().isUsing2FA(),
				user.get().getUsername()));
		LOGGER.info(new ParameterizedMessage("The Verification code entered is {} and the valid code is {}",
				verificationCode, validVerificationCode));

		if (verificationCodeNotGenerated(user.get(), master.get())) {
			throw new VerificationCodeNotGeneratedException(verificationCodeNotGeneratedMessage);
		}

		if (verificationCodeNotFilled(user.get(), master.get(), verificationCode)) {
			throw new VerificationCodeNotFilledException(verificationCodeNotFilledMessage);
		}

		if (doesVerificationCodeMatch(user.get(), master.get(), verificationCode, validVerificationCode)) {
			throw new VerificationCodeDoesNotMatchException(verificationCodeDoesNotMatchMessage);
		}

		userService.clearSecretKey(user.get());
	}

	private boolean isUserInactive(final Users user) {
		boolean isInactive = !user.isActive();
		LOGGER.info(new ParameterizedMessage("The user is inactive {}", isInactive));
		return isInactive;
	}

	private boolean doesVerificationCodeMatch(final Users user, final Master master, final String verificationCode,
			final String validVerificationCode) {
		boolean doesVerificationCodeMatch = master.isEnable2FA() && user.isUsing2FA()
				&& (ImpactNumberUtils.isNotAValidLong(verificationCode)
						|| HydraStringUtils.notEquals(verificationCode, validVerificationCode));
		LOGGER.info(new ParameterizedMessage("The verification code does not match {}", doesVerificationCodeMatch));
		return doesVerificationCodeMatch;
	}

	private boolean verificationCodeNotFilled(final Users user, final Master master, final String verificationCode) {
		boolean verificationCodeNotFilled = master.isEnable2FA() && user.isUsing2FA()
				&& HydraStringUtils.isNotBlank(user.getSecret2FAKey()) && StringUtils.isBlank(verificationCode);
		LOGGER.info(new ParameterizedMessage("The verification code is not filled {}", verificationCodeNotFilled));
		return verificationCodeNotFilled;
	}

	private boolean verificationCodeNotGenerated(final Users user, final Master master) {
		boolean verificationCodeNotGenerated = master.isEnable2FA() && user.isUsing2FA()
				&& StringUtils.isBlank(user.getSecret2FAKey());
		LOGGER.info(
				new ParameterizedMessage("The verification code is not generated {}", verificationCodeNotGenerated));
		return verificationCodeNotGenerated;
	}

	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
