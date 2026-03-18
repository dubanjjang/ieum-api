package com.dubanjang.ieum.auth.presentation;

import com.dubanjang.ieum.auth.application.AuthService;
import com.dubanjang.ieum.auth.application.dto.AuthResult;
import com.dubanjang.ieum.auth.presentation.request.UserLoginRequest;
import com.dubanjang.ieum.auth.presentation.view.LoginView;
import com.dubanjang.ieum.common.config.properties.JwtProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtProperties jwtProperties;

    @PostMapping("/login")
    public ResponseEntity<LoginView> login(
            @RequestBody UserLoginRequest userLoginRequest,
            HttpServletResponse response
    ) {
        AuthResult authResult = authService.login(userLoginRequest);
        authService.setTokenResponse(response, authResult);
        return ResponseEntity.ok(new LoginView(authResult.userId(), authResult.status()));
    }

    @PostMapping("/reissue")
    public ResponseEntity<Void> reissue(
            @CookieValue(name = "refresh_token") String refreshToken,
            HttpServletResponse response
    ) {
        AuthResult authResult = authService.reissueToken(refreshToken);
        authService.setTokenResponse(response, authResult);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
