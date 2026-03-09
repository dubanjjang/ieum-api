package com.dubanjang.ieum.common.config.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "oauth.google")
public record GoogleOAuthProperties(
        Client client,
        Url url
) {
    public record Client(
            @NotBlank String id,
            @NotBlank String secret,
            @NotBlank String redirectUri
    ) {}

    public record Url(
            @NotBlank String auth,
            @NotBlank String token,
            @NotBlank String userInfo
    ) {}
}
