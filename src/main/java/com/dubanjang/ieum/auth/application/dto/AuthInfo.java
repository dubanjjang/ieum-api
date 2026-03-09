package com.dubanjang.ieum.auth.application.dto;

import com.dubanjang.ieum.user.domain.UserStatus;

import java.time.LocalDateTime;

public record AuthInfo(
        long userId,
        UserStatus status,
        String username,
        LocalDateTime withdrawnAt
) {
}
