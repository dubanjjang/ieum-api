package com.dubanjang.ieum.auth.presentation.view;

import com.dubanjang.ieum.user.domain.UserStatus;

public record LoginView(
        long userId,
        UserStatus status
) {
}
