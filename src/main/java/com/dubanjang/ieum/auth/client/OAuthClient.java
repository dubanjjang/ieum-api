package com.dubanjang.ieum.auth.client;

import com.dubanjang.ieum.auth.client.response.UserInfo;
import com.dubanjang.ieum.auth.domain.OAuthVender;

import java.net.URI;

public interface OAuthClient {

    OAuthVender getOAuthVender();
    String getAccessToken(String code);
    UserInfo getUserInfo(String token);
    URI getLoginPageUri();
}
