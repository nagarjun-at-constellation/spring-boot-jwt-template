package com.bancocaminos.impactbackendapi.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { MessageConfiguration.class })
public class MessageConfigurationTest {

    @Autowired
    private MessageConfiguration messageConfiguration;

    @Test
    public void messageConfiguration() {
        ReloadableResourceBundleMessageSource messageSource = (ReloadableResourceBundleMessageSource) messageConfiguration
                .messageSource();

        String message = messageSource.getMessage("SendSMS.message", null, Locale.ENGLISH);
        assertEquals("Your Verification code is ", message);

        message = messageSource.getMessage("Layout.Copyright", null, Locale.ENGLISH);
        assertEquals("©2022 - Constellation", message);
    }
}
