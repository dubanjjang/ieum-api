package com.dubanjang.ieum.auth.client.response;

import com.dubanjang.ieum.auth.application.dto.GoogleUserInfoResponse;
import com.dubanjang.ieum.auth.domain.OAuthVender;

public record UserInfo(
        String venderId,
        OAuthVender oAuthVender,
        String name
) {
    public static UserInfo from(GoogleUserInfoResponse googleUserInfoResponse) {
        return new UserInfo(
                googleUserInfoResponse.id(),
                OAuthVender.GOOGLE,
                googleUserInfoResponse.name()
        );
    }

    public String getPrefixedVenderId() {
        return oAuthVender.getPrefix() + venderId;
    }
}
