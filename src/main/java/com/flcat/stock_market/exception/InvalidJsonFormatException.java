package com.flcat.stock_market.exception;

// InvalidJsonFormatException.java
public class InvalidJsonFormatException extends RuntimeException {
    public InvalidJsonFormatException(String message) {
        super(message);
    }

    public InvalidJsonFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
