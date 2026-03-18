package com.dubanjang.ieum.auth.domain.repository;

import com.dubanjang.ieum.auth.domain.OAuthLink;
import com.dubanjang.ieum.auth.domain.OAuthLinkId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthRepository extends JpaRepository<OAuthLink, OAuthLinkId> {
}
