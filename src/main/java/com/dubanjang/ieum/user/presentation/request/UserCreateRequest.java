package com.dubanjang.ieum.user.presentation.request;

import lombok.Builder;

@Builder
public record UserCreateRequest(
        String email,
        String password,
        String username
) {
}
