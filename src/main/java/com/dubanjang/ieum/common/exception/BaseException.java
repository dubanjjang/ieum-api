package com.dubanjang.ieum.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    private final String code;
    private final HttpStatus httpStatus;

    public BaseException(ErrorCode errorCode, HttpStatus httpStatus) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.httpStatus = httpStatus;
    }
}
