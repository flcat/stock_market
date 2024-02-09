package com.flcat.stock_market.config;

import com.flcat.stock_market.code.ResponseCode;
import com.flcat.stock_market.exception.DecryptException;
import com.flcat.stock_market.exception.EncryptException;
import com.flcat.stock_market.service.ExceptionLogService;
import com.flcat.stock_market.vo.common.ResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {

    private final String INVALID_KEY_MESSAGE = "java.security.InvalidKeyException";
    private final String BAD_KEY_MESSAGE = "javax.crypto.BadPaddingException";
    private final String INVALID_TYPE_MESSAGE = "InvalidEncryptTypeException";
    private final ExceptionLogService exceptionLogService;

    @ExceptionHandler(EncryptException.class)
    public <T> ResponseVO<T> encryptExceptionHandler(EncryptException e) {
        if (e.getMessage().contains(INVALID_KEY_MESSAGE)) {
            return insertExceptionLog(ResponseCode.KEY_LENGTH_ERROR);
        } else if (e.getMessage().contains(INVALID_TYPE_MESSAGE)) {
            return insertExceptionLog(ResponseCode.INVALID_MODE_ERROR);
        }
        return insertExceptionLog(ResponseCode.ENCRYPTION_ERROR);
    }

    @ExceptionHandler(DecryptException.class)
    public <T> ResponseVO<T> decryptExceptionHandler(DecryptException e) {
        if (e.getMessage().contains(INVALID_KEY_MESSAGE)) {
            return insertExceptionLog(ResponseCode.KEY_LENGTH_ERROR);
        } else if (e.getMessage().contains(BAD_KEY_MESSAGE)) {
            return insertExceptionLog(ResponseCode.BAD_KEY_ERROR);
        }
        return insertExceptionLog(ResponseCode.DECRYPTION_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public <T> ResponseVO<T> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String defaultMessage = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(","));
        String errorMessage = ResponseCode.NOT_VALID_ERROR.getMessage() + " [" + defaultMessage +"]";
        exceptionLogService.insertLogMessage(errorMessage);
        return ResponseVO.fail(ResponseCode.NOT_VALID_ERROR.getCode(), errorMessage);
    }

    private <T> ResponseVO<T> insertExceptionLog(ResponseCode responseCode) {

        exceptionLogService.insertLogMessage(responseCode.getMessage());
        return ResponseVO.fail(responseCode);
    }
}
