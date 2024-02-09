package com.flcat.stock_market.vo.common;

import com.flcat.stock_market.code.ResponseCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseVO<T> {

    private String resultCode;
    private String resultMessage;
    private T body;

    public static <T> ResponseVO<T> success(T body) {
        return of(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), body);
    }

    public static <T> ResponseVO<T> fail(ResponseCode code) {
        return of(code.getCode(), code.getMessage(), null);
    }

    public static <T> ResponseVO<T> fail(String errorCode, String errorMsg) {
        return of(errorCode, errorMsg, null);
    }

    public static <T> ResponseVO<T> of(String errorCode, String errorMsg, T body) {
        ResponseVO<T> responseModel = new ResponseVO<>();
        responseModel.setResultCode(errorCode);
        responseModel.setResultMessage(errorMsg);
        responseModel.setBody(body);
        return responseModel;
    }
}
