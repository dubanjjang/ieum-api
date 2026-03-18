package com.dubanjang.ieum.auth.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class OAuthLinkId implements Serializable {
    private String venderId;

    @Enumerated(EnumType.STRING)
    private OAuthVender vender;
}
