package com.partnr.saas.exception;

public class UnauthorizedException extends ApiException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
