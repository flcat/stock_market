package com.flcat.stock_market.exception;

// InvalidPriceFormatException.java
public class InvalidPriceFormatException extends RuntimeException {
    public InvalidPriceFormatException(String message) {
        super(message);
    }

    public InvalidPriceFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
