package com.flcat.stock_market.exception;

public class FailedResponseException extends Exception {

    public FailedResponseException(Object object) {
        super(object.toString());
    }

    public FailedResponseException(String message) {
        super(message);
    }
}
