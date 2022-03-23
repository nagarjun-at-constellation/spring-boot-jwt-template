package com.bancocaminos.impactbackendapi.core.exception.aws;

public class UnableToPublishMessageException extends RuntimeException {
    public UnableToPublishMessageException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
