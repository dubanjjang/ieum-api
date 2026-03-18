package com.dubanjang.ieum.auth.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "oauth_link")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OAuthLink {

    @EmbeddedId
    private OAuthLinkId id;

    @Column
    private long userId;
}
