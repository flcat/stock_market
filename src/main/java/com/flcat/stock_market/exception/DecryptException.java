package com.flcat.stock_market.exception;

public class DecryptException extends RuntimeException {
    public DecryptException() {

    }

    public DecryptException(Exception e) {
        super(e);
    }
}
