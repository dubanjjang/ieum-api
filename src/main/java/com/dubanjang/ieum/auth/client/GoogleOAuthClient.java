package com.dubanjang.ieum.auth.client;

import com.dubanjang.ieum.auth.application.dto.GoogleUserInfoResponse;
import com.dubanjang.ieum.auth.client.response.UserInfo;
import com.dubanjang.ieum.auth.domain.OAuthVender;
import com.dubanjang.ieum.common.config.properties.GoogleOAuthProperties;
import com.dubanjang.ieum.common.exception.ErrorCode;
import com.dubanjang.ieum.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoogleOAuthClient implements OAuthClient {

    private final RestClient restClient;
    private final GoogleOAuthProperties googleOAuthProperties;

    @Override
    public OAuthVender getOAuthVender() {
        return OAuthVender.GOOGLE;
    }

    @Override
    public String getAccessToken(String code) {
        Map response = restClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .body(Map.of(
                        "client_id", googleOAuthProperties.client().id(),
                        "client_secret", googleOAuthProperties.client().secret(),
                        "code", code,
                        "redirect_uri", googleOAuthProperties.client().redirectUri(),
                        "grant_type", "authorization_code"
                ))
                .retrieve()
                .body(Map.class);
        return response.get("access_token").toString();
    }

    @Override
    public UserInfo getUserInfo(String accessToken) {
        GoogleUserInfoResponse googleUserInfoResponse = restClient.get()
                .uri("https://www.googleapis.com/oauth2/v2/userinfo")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .body(GoogleUserInfoResponse.class);
        if (googleUserInfoResponse == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_USER);
        }
        return UserInfo.from(googleUserInfoResponse);
    }

    public URI getLoginPageUri() {
        String googleAuthUrl = UriComponentsBuilder
                .fromUriString(googleOAuthProperties.url().auth())
                .queryParam("client_id", googleOAuthProperties.client().id())
                .queryParam("redirect_uri", googleOAuthProperties.client().redirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", "email%20profile")
                .build()
                .toUriString();

        return URI.create(googleAuthUrl);
    }
}
