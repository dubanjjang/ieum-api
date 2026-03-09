package com.dubanjang.ieum.user.presentation.view;

import java.time.LocalDateTime;

public record WithdrawView(
        long userId,
        LocalDateTime deletedAt
) {
}
