package com.dubanjang.ieum.user.presentation;

import com.dubanjang.ieum.auth.application.dto.AuthInfo;
import com.dubanjang.ieum.common.annotation.AuthUser;
import com.dubanjang.ieum.user.application.UserService;
import com.dubanjang.ieum.user.presentation.request.PasswordChangeRequest;
import com.dubanjang.ieum.user.presentation.request.UserCreateRequest;
import com.dubanjang.ieum.user.presentation.request.UsernameChangeRequest;
import com.dubanjang.ieum.user.presentation.view.UserInfoView;
import com.dubanjang.ieum.user.presentation.view.WithdrawView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoView> getUserInfo(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoView> getCurrentUser(@AuthUser AuthInfo authInfo) {
        return ResponseEntity.ok(userService.getUserInfo(authInfo.userId()));
    }

    @PatchMapping("/me/nickname")
    public ResponseEntity<Void> changeUsername(
            @AuthUser AuthInfo authInfo,
            @RequestBody @Valid UsernameChangeRequest request
    ) {
        userService.changeUsername(authInfo.userId(), request.username());
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @AuthUser AuthInfo authInfo,
            @RequestBody @Valid PasswordChangeRequest request
    ) {
        userService.changePassword(authInfo.userId(), request);
        return ResponseEntity.ok(null);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(
            @RequestBody UserCreateRequest userCreateRequest
    ) {
        userService.createUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<WithdrawView> deleteUser(
            @AuthUser AuthInfo authInfo
    ) {
        return ResponseEntity.ok(userService.withdrawUser(authInfo.userId()));
    }

    @PatchMapping("/me/reactive")
    public ResponseEntity<Void> reactiveUser(
            @AuthUser AuthInfo authInfo
    ) {
        userService.reactiveUser(authInfo.userId());
        return ResponseEntity.ok(null);
    }

}
