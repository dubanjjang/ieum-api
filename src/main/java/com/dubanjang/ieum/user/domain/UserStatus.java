package com.dubanjang.ieum.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    ACTIVE("활성", "정상적으로 서비스를 이용 중인 상태"),
    WITHDRAWN("탈퇴", "사용자 요청으로 탈퇴 처리된 상태"),
    BANNED("정지", "운영 정책 위반으로 차단된 상태");

    private final String title;
    private final String description;
}