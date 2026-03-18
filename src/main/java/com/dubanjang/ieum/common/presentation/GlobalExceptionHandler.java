package com.dubanjang.ieum.common.presentation;

import com.dubanjang.ieum.common.exception.BaseException;
import com.dubanjang.ieum.common.presentation.view.ErrorView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorView> handleBaseException(BaseException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorView(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorView> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorView errorView = new ErrorView("INVALID_PARAMETER", "요청 본문이 올바르지 않습니다.", errors);

        return ResponseEntity.badRequest().body(errorView);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorView> handleException(Exception e) {
        log.error(e.getMessage(), e);
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorView("SERVER_50000", "서버에 문제가 발생하였습니다."));
    }
}
