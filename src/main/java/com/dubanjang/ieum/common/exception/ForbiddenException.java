package com.dubanjang.ieum.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ForbiddenException extends BaseException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.FORBIDDEN);
    }
}
