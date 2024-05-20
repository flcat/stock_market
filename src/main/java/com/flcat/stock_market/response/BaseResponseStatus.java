package com.flcat.stock_market.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BaseResponseStatus {

    SUCCESS(true, HttpStatus.OK.value(), "success"),

    NOT_FOUND(false, HttpStatus.NOT_FOUND.value(), "Not Found");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    public String toString() {
        return "{" + "\"isSuccess\" : " + "\""+isSuccess+"\"" +
                "\"code\" : " + "\""+code+"\"" +
                "\"message\" : " + "\""+message+"\"" + "}";
    }
}
