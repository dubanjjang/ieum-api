package com.dubanjang.ieum.user.domain;

import com.dubanjang.ieum.common.exception.BadRequestException;
import com.dubanjang.ieum.common.exception.ErrorCode;
import com.dubanjang.ieum.term.domain.EssentialTermVersion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Builder.Default
    @Column(nullable = false)
    private boolean isEmailVerified = false;

    @Setter
    @Column(nullable = true)
    private String password;

    @Setter
    @Column(nullable = false)
    private String username;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EssentialTermVersion essentialTermVersion = EssentialTermVersion.NOT_AGREED;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime withdrawnAt = null;

    public LocalDateTime withdraw() {
        if (this.status == UserStatus.WITHDRAWN) throw new BadRequestException(ErrorCode.ALREADY_WITHDRAWN_USER);

        this.withdrawnAt = LocalDateTime.now().plusDays(7);
        this.status = UserStatus.WITHDRAWN;

        return this.withdrawnAt;
    }

    public void reactive() {
        if (this.status == UserStatus.ACTIVE) throw new BadRequestException(ErrorCode.ALREADY_ACTIVE_USER);

        this.status = UserStatus.ACTIVE;
        this.withdrawnAt = null;
    }
}
