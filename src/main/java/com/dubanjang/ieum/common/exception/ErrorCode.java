package com.dubanjang.ieum.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Auth
    INVALID_LOGIN_INFO("AUTH_40100", "일치하는 인증 정보를 찾을 수 없습니다."),
    EXPIRED_TOKEN("AUTH_40101", "만료된 토큰입니다."),
    INVALID_TOLEN("AUTH_40102", "유효하지 않은 토큰입니다."),

    // User
    NOT_FOUND_USER("USER_40400", "일치하는 유저를 찾을 수 없습니다."),
    DUPLICATED_USER_EMAIL("USER_40900", "이미 가입된 이메일 주소입니다."),
    NOT_EQUALS_NEW_PASSWORD("USER_40000", "변경할 비밀번호가 일치하지 않습니다."),
    NOT_EQUALS_OLD_PASSWORD("USER_40001", "기존의 패스워드가 일치하지 않습니다"),
    ALREADY_WITHDRAWN_USER("USER_40002", "이미 탈퇴처리된 유저입니다."),
    ALREADY_ACTIVE_USER("USER_40003", "이미 활성화된 유저입니다.");

    private final String code;
    private final String message;
}
