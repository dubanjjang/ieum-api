package com.dubanjang.ieum.auth.presentation;


import com.dubanjang.ieum.auth.application.AuthService;
import com.dubanjang.ieum.auth.application.OAuthService;
import com.dubanjang.ieum.auth.application.dto.AuthResult;
import com.dubanjang.ieum.auth.client.response.UserInfo;
import com.dubanjang.ieum.auth.domain.OAuthVender;
import com.dubanjang.ieum.auth.presentation.view.LoginView;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/oauth2")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;
    private final AuthService authService;

    @Value("${oauth.frontend.landing-uri}")
    private String OAUTH_FRONTEND_LANDING_URL;

    @GetMapping("/{vender}/login")
    public ResponseEntity<Void> redirectToOAuthLoginPage(
            @PathVariable OAuthVender vender
    ) {
        URI loginPageUri = oAuthService.getLoginPageUri(vender);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(loginPageUri)
                .build();
    }

    @GetMapping("/{vender}/redirect")
    public ResponseEntity<Void> redirectToFrontPage(
            @PathVariable OAuthVender vender,
            @RequestParam String code
    ) {
        URI oauthFrontendLangdingUri = createFrontPageUri(vender, code);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(oauthFrontendLangdingUri)
                .build();
    }

    @PostMapping("/{vender}/login")
    public ResponseEntity<LoginView> oauthLogin(
            @PathVariable OAuthVender vender,
            @RequestParam String code,
            HttpServletResponse response
    ) {
        String accessToken = oAuthService.getAccessToken(vender, code);
        UserInfo userInfo = oAuthService.getUserInfo(vender, accessToken);
        AuthResult authResult = oAuthService.linkToIeumAccount(userInfo, vender);
        authService.setTokenResponse(response, authResult);
        return ResponseEntity.ok(new LoginView(authResult.userId(), authResult.status()));
    }

    private URI createFrontPageUri(
            OAuthVender vender,
            String code
    ) {
        String frontPageUri = UriComponentsBuilder
                .fromUriString(OAUTH_FRONTEND_LANDING_URL)
                .queryParam("vender", vender.name().toLowerCase())
                .queryParam("code", code)
                .build()
                .toUriString();
        return URI.create(frontPageUri);
    }
}

//https://accounts.google.com/o/oauth2/v2/auth?client_id=422593134138-n8s0nkuc9md2m3cqovil55ip36natvtm.apps.googleusercontent.com&redirect_uri=http://localhost:8080/api/v1/oauth2/code/google&response_type=code&scope=email profile