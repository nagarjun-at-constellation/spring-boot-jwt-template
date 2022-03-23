package com.bancocaminos.impactbackendapi.core.exceptions.aws;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bancocaminos.impactbackendapi.core.exception.aws.UnableToPublishMessageException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class UnableToPublishMessageExceptionTest {

    @Test
    public void successfulExceptionTest() {
        assertThrows(
                UnableToPublishMessageException.class,
                () -> throwUnableToPublishError(),
                "Expected UnableToPublishMessageException() to throw, but it didn't");
    }

    private Object throwUnableToPublishError() {
        throw new UnableToPublishMessageException(null, null);
    }

    @Test
    public void superClassTest() {
        assertTrue((new UnableToPublishMessageException(null, null)) instanceof RuntimeException);
        assertTrue(RuntimeException.class.isAssignableFrom(UnableToPublishMessageException.class));
    }
}
