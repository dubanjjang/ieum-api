package com.dubanjang.ieum.common.presentation.view;

import com.dubanjang.ieum.common.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Optional;

public record ErrorView(
        String code,
        String message,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        Map<String, String> data
) {

    public ErrorView(String code, String message) {
        this(code, message, null);
    }
}
