package com.dubanjang.ieum.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthVender {
    GOOGLE("google_"),
    NAVER("naver_"),
    KAKAO("kakao_");

    private final String prefix;
}
