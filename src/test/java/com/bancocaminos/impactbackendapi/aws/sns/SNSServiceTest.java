package com.bancocaminos.impactbackendapi.aws.sns;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishResult;
import com.bancocaminos.impactbackendapi.aws.configuration.SNSConfig;
import com.bancocaminos.impactbackendapi.core.exception.aws.UnableToPublishMessageException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SNSService.class })
public class SNSServiceTest {

    @MockBean
    private AmazonSNS amazonSNS;

    @Autowired
    private SNSService snsService;

    @MockBean
    private SNSConfig snsConfig;

    @Test
    public void successfullyPublished() {
        PublishResult publishResult = new PublishResult();
        publishResult.setMessageId("1");
        when(snsConfig.snsClient()).thenReturn(amazonSNS);
        when(amazonSNS.publish(any())).thenReturn(publishResult);
        snsService.publishTextSMS("Your verification code is 123456", "666666666");
        verify(amazonSNS, times(1)).publish(any());
    }

    @Test
    public void exceptionWhenPublished() {
        when(snsConfig.snsClient()).thenReturn(amazonSNS);
        when(amazonSNS.publish(any())).thenThrow(new UnableToPublishMessageException(null, null));

        UnableToPublishMessageException thrown = Assertions
                .assertThrows(UnableToPublishMessageException.class,
                        () -> snsService.publishTextSMS("Your verification code is 123456", "666666666"),
                        "UnableToPublishMessageException error was expected");
        Assertions.assertEquals("There was an error publishing the message", thrown.getMessage());
        verify(amazonSNS, times(1)).publish(any());
    }

}
