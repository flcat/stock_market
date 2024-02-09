package com.flcat.stock_market.exception;

public class EncryptException extends RuntimeException {
    public EncryptException() {
        super();
    }

    public EncryptException(Exception e) {
        super(e);
    }
}
