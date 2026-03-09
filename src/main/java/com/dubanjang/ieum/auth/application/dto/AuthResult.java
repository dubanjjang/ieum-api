package com.dubanjang.ieum.auth.application.dto;

import com.dubanjang.ieum.user.domain.UserStatus;
import lombok.Builder;

@Builder
public record AuthResult(
        long userId,
        String accessToken,
        String refreshToken,
        UserStatus status
) {
}
