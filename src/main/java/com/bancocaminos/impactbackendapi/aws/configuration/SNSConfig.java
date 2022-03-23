package com.bancocaminos.impactbackendapi.aws.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.bancocaminos.impactbackendapi.aws.AWSProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SNSConfig {

    @Autowired
    private AWSProperties awsProperties;

    @Bean
    public AmazonSNS snsClient() {
        BasicAWSCredentials awsCredentialsProvider = new BasicAWSCredentials(awsProperties.getAccessKey(),
                awsProperties.getSecretKey());
        return AmazonSNSClient.builder().withRegion(awsProperties.getRegion())
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentialsProvider)).build();
    }
}
