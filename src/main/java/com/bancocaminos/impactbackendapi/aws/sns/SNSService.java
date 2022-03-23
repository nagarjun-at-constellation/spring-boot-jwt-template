package com.bancocaminos.impactbackendapi.aws.sns;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.bancocaminos.impactbackendapi.aws.configuration.SNSConfig;
import com.bancocaminos.impactbackendapi.core.exception.aws.UnableToPublishMessageException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SNSService {

	private static Logger LOGGER = LogManager.getLogger(SNSService.class);
	private static final String AWS_SNS_SMS_TYPE = "AWS.SNS.SMS.SMSType";
	private static final String AWS_SNS_SMS_TYPE_VALUE = "Transactional";
	private static final String AWS_SNS_DATA_TYPE = "String";
	int requestTimeout = 3000;

	@Autowired
	private SNSConfig snsConfig;

	public void publishTextSMS(String message, String phoneNumber) throws UnableToPublishMessageException {
		try {
			AmazonSNS snsClient = snsConfig.snsClient();
			Map<String, MessageAttributeValue> smsAttributes = buildMessageAttributes();
			PublishRequest request = new PublishRequest().withMessage(message).withPhoneNumber(phoneNumber)
					.withMessageAttributes(smsAttributes).withSdkRequestTimeout(requestTimeout);
			PublishResult result = snsClient.publish(request);
			LOGGER.info(new ParameterizedMessage("Message ID {}, Message Sent. Status was {}", result.getMessageId(),
					fetchStatusCode(result)));

		} catch (RuntimeException e) {
			LOGGER.error(new ParameterizedMessage("There was an error publishing the message {}", e.getMessage()), e);
			throw new UnableToPublishMessageException("There was an error publishing the message", e);
		}
	}

	private int fetchStatusCode(PublishResult result) {
		return result.getSdkHttpMetadata() == null ? 400 : result.getSdkHttpMetadata().getHttpStatusCode();
	}

	private Map<String, MessageAttributeValue> buildMessageAttributes() {
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<>();
		MessageAttributeValue messageAttributeValue = new MessageAttributeValue()
				.withStringValue(AWS_SNS_SMS_TYPE_VALUE).withDataType(AWS_SNS_DATA_TYPE);
		smsAttributes.put(AWS_SNS_SMS_TYPE, messageAttributeValue);
		return smsAttributes;
	}
}
