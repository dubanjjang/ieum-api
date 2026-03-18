package com.dubanjang.ieum.common.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExsitsException extends BaseException {
    public AlreadyExsitsException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.CONFLICT);
    }
}
