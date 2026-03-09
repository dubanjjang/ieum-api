package com.dubanjang.ieum.user.presentation.view;

import com.dubanjang.ieum.common.annotation.BaseSnakeDto;
import com.dubanjang.ieum.user.domain.User;
import lombok.Builder;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@Builder
@BaseSnakeDto
public record UserInfoView(
        long userId,
        String email,
        String nickname,
        String status,
        LocalDateTime joinedAt
) {
    public static UserInfoView from(User user) {
        return UserInfoView.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getUsername())
                .status(user.getStatus().getTitle())
                .joinedAt(user.getJoinedAt())
                .build();
    }
}