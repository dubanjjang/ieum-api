package com.dubanjang.ieum.auth.presentation.request;

public record UserLoginRequest(
        String email,
        String password
) {
}
