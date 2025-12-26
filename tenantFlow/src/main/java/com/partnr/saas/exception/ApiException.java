package com.partnr.saas.exception;

public abstract class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }
}
