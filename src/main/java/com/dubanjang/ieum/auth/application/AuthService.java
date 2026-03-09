package com.dubanjang.ieum.auth.application;

import com.dubanjang.ieum.auth.application.dto.AuthResult;
import com.dubanjang.ieum.auth.application.dto.AuthInfo;
import com.dubanjang.ieum.auth.presentation.request.UserLoginRequest;
import com.dubanjang.ieum.common.config.properties.JwtProperties;
import com.dubanjang.ieum.auth.util.JwtProvider;
import com.dubanjang.ieum.common.domain.RedisRepository;
import com.dubanjang.ieum.common.exception.ErrorCode;
import com.dubanjang.ieum.common.exception.NotFoundException;
import com.dubanjang.ieum.common.exception.UnauthorizedException;
import com.dubanjang.ieum.user.domain.User;
import com.dubanjang.ieum.user.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;
    private final JwtProvider jwtProvider;
    private final RedisRepository redisRepository;

    public AuthResult login(UserLoginRequest userLoginRequest) {
        User user = userRepository.findByEmail(userLoginRequest.email())
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_LOGIN_INFO));

        boolean isMatched = BCrypt.checkpw(userLoginRequest.password(), user.getPassword());
        if (!isMatched) {
            throw new UnauthorizedException(ErrorCode.INVALID_LOGIN_INFO);
        }

        return generateTokens(user);
    }

    public AuthResult reissueToken(String refreshToken) {
        AuthInfo authInfo;
        try {
            authInfo = jwtProvider.validateToken(refreshToken);
        } catch (Exception e) {
            throw new UnauthorizedException(ErrorCode.EXPIRED_TOKEN);
        }

        User user = userRepository.findById(authInfo.userId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));

        String redisKey = String.format("RT:%s", authInfo.userId());
        String savedToken = redisRepository.getData(redisKey);
        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new UnauthorizedException(ErrorCode.INVALID_TOLEN);
        }

        return generateTokens(user);
    }

    public AuthResult generateTokens(User user) {
        String accessToken = jwtProvider.createToken(user, JwtProvider.TokenType.ACCESS_TOKEN);
        String refreshToken = jwtProvider.createToken(user, JwtProvider.TokenType.REFRESH_TOKEN);

        String key = String.format("RT:%s", user.getId());
        redisRepository.setData(key, refreshToken, jwtProperties.refreshTokenExpiration());

        return AuthResult.builder()
                .userId(user.getId())
                .accessToken(String.format("Ieum %s", accessToken))
                .refreshToken(refreshToken)
                .status(user.getStatus())
                .build();
    }

    public void setTokenResponse(HttpServletResponse response, AuthResult authResult) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", authResult.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtProperties.refreshTokenExpiration() / 1000)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        response.addHeader("x-ieum-access", authResult.accessToken());
    }
}
