package com.dubanjang.ieum.auth.application;

import com.dubanjang.ieum.auth.application.dto.AuthResult;
import com.dubanjang.ieum.auth.client.OAuthClient;
import com.dubanjang.ieum.auth.client.response.UserInfo;
import com.dubanjang.ieum.auth.domain.OAuthLink;
import com.dubanjang.ieum.auth.domain.OAuthLinkId;
import com.dubanjang.ieum.auth.domain.repository.OAuthRepository;
import com.dubanjang.ieum.auth.domain.OAuthVender;
import com.dubanjang.ieum.common.exception.ErrorCode;
import com.dubanjang.ieum.common.exception.NotFoundException;
import com.dubanjang.ieum.user.domain.User;
import com.dubanjang.ieum.user.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OAuthService {
    private final AuthService authService;
    private final OAuthRepository oAuthRepository;
    private final UserRepository userRepository;
    private final Map<OAuthVender, OAuthClient> oAuthClientsMap;

    public OAuthService(
            AuthService authService,
            OAuthRepository oAuthRepository,
            UserRepository userRepository,
            List<OAuthClient> oAuthClients
    ) {
        this.authService = authService;
        this.oAuthRepository = oAuthRepository;
        this.userRepository = userRepository;
        this.oAuthClientsMap = oAuthClients.stream()
                .collect(
                        Collectors.toMap(
                                OAuthClient::getOAuthVender,
                                service -> service
                        )
                );
    }

    public String getAccessToken(OAuthVender vender, String code) {
        OAuthClient oAuthClient = oAuthClientsMap.get(vender);
        return oAuthClient.getAccessToken(code);
    }

    public UserInfo getUserInfo(OAuthVender vender, String accessToken) {
        OAuthClient oAuthClient = oAuthClientsMap.get(vender);
        return oAuthClient.getUserInfo(accessToken);
    }

    @Transactional
    public AuthResult linkToIeumAccount(UserInfo userInfo, OAuthVender vender) {
        OAuthLinkId id = new OAuthLinkId(userInfo.venderId(), vender);

        OAuthLink oAuthLink = oAuthRepository.findById(id)
                .orElseGet(() -> createNewUserAndLink(userInfo, id));

        User user = userRepository.findById(oAuthLink.getUserId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));

        return authService.generateTokens(user);
    }

    public URI getLoginPageUri(OAuthVender vender) {
        OAuthClient oAuthClient = oAuthClientsMap.get(vender);
        return oAuthClient.getLoginPageUri();
    }

    private OAuthLink createNewUserAndLink(UserInfo userInfo, OAuthLinkId id) {
        String autoCreatedEmail = String.format("%s@ieum.com", userInfo.getPrefixedVenderId());

        User newUser = User.builder()
                .email(autoCreatedEmail)
                .password(null)
                .username(userInfo.name())
                .build();
        userRepository.save(newUser);

        return oAuthRepository.save(new OAuthLink(id, newUser.getId()));
    }
}
