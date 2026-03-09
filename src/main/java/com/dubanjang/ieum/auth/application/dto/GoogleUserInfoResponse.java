package com.dubanjang.ieum.auth.application.dto;

import com.dubanjang.ieum.common.annotation.BaseSnakeDto;

@BaseSnakeDto
public record GoogleUserInfoResponse(
    String id,
    String email,
    boolean verifiedEmail,
    String name,
    String givenName,
    String familyName,
    String picture,
    String locale
) {
}
