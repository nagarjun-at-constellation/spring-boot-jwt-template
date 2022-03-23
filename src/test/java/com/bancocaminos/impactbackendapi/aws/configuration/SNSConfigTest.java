package com.bancocaminos.impactbackendapi.aws.configuration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.sns.AmazonSNS;
import com.bancocaminos.impactbackendapi.aws.AWSProperties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class SNSConfigTest {

    @MockBean
    private AWSProperties awsProperties;

    @Autowired
    private SNSConfig snsConfig;

    @TestConfiguration
    static class SNSConfigTestContextConfiguration {

        @Bean
        public SNSConfig snsConfig() {
            return new SNSConfig();
        }

    }

    @Test
    public void succesfulBuild() {
        when(awsProperties.getRegion()).thenReturn("value");
        when(awsProperties.getAccessKey()).thenReturn("accessKey");
        when(awsProperties.getSecretKey()).thenReturn("secretKey");
        AmazonSNS amazonSNS = snsConfig.snsClient();
        assertNotNull(amazonSNS);
        verify(awsProperties, times(1)).getRegion();
        verify(awsProperties, times(1)).getAccessKey();
        verify(awsProperties, times(1)).getAccessKey();
    }
}
