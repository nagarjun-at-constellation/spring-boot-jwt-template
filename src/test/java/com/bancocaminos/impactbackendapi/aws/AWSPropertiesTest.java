package com.bancocaminos.impactbackendapi.aws;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bancocaminos.impactbackendapi.ImpactBackEndApiApplication;
import com.bancocaminos.impactbackendapi.aws.configuration.SNSConfig;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ImpactBackEndApiApplication.class, SNSConfig.class })
@TestPropertySource("classpath:test.properties")
public class AWSPropertiesTest {

    @Autowired
    private AWSProperties awsProperties;

    @MockBean
    private SNSConfig snsConfig;

    @Test
    public void awspropertiesSuccessTest() {
        assertEquals("eu-west-1", awsProperties.getRegion());
    }

}
