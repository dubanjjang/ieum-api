package com.dubanjang.ieum.auth.util;

import com.dubanjang.ieum.auth.application.dto.AuthInfo;
import com.dubanjang.ieum.common.config.properties.JwtProperties;
import com.dubanjang.ieum.user.domain.User;
import com.dubanjang.ieum.user.domain.UserStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtProvider {
    private final SecretKey key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtProvider(JwtProperties jwtProperties) {
        this.key = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = jwtProperties.accessTokenExpiration();
        this.refreshTokenExpiration = jwtProperties.refreshTokenExpiration();
    }

    public String createToken(User user, TokenType tokenType) {
        long expirationTime = switch (tokenType) {
            case ACCESS_TOKEN -> accessTokenExpiration;
            case REFRESH_TOKEN -> refreshTokenExpiration;
        };

        Date now = new Date();
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("status", user.getStatus())
                .claim("username", user.getUsername())
                .claim("withdrawnAt", user.getWithdrawnAt())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationTime))
                .signWith(key)
                .compact();
    }

    public AuthInfo validateToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key) // 주입받은 Signing Key
                .build()
                .parseSignedClaims(token)
                .getPayload();
        long userId = Long.parseLong(claims.getSubject());
        String statusStr = claims.get("status", String.class);
        UserStatus status = UserStatus.valueOf(statusStr);

        String username = claims.get("username", String.class);
        LocalDateTime withdrawnAt = LocalDateTime.parse(claims.get("withdrawnAt", String.class));
        return new AuthInfo(userId, status, username, withdrawnAt);
    }

    public enum TokenType {
        REFRESH_TOKEN,
        ACCESS_TOKEN
    }
}