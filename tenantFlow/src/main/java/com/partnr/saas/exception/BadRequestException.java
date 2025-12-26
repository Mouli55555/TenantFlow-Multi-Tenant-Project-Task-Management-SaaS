package com.partnr.saas.exception;

public class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super(message);
    }
}
