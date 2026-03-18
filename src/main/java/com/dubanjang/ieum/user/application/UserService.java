package com.dubanjang.ieum.user.application;

import com.dubanjang.ieum.common.exception.AlreadyExsitsException;
import com.dubanjang.ieum.common.exception.BadRequestException;
import com.dubanjang.ieum.common.exception.ErrorCode;
import com.dubanjang.ieum.common.exception.NotFoundException;
import com.dubanjang.ieum.user.domain.User;
import com.dubanjang.ieum.user.domain.repository.UserRepository;
import com.dubanjang.ieum.user.presentation.request.PasswordChangeRequest;
import com.dubanjang.ieum.user.presentation.request.UserCreateRequest;
import com.dubanjang.ieum.user.presentation.view.UserInfoView;
import com.dubanjang.ieum.user.presentation.view.WithdrawView;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfoView getUserInfo(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));

        return UserInfoView.from(user);
    }

    public long createUser(UserCreateRequest userCreateRequest) {
        boolean existsUser = userRepository.existsUserByEmail(userCreateRequest.email());
        if (existsUser) {
            throw new AlreadyExsitsException(ErrorCode.DUPLICATED_USER_EMAIL);
        }

        String hashedPassword = BCrypt.hashpw(userCreateRequest.password(), BCrypt.gensalt());

        User user = User.builder()
                .email(userCreateRequest.email())
                .password(hashedPassword)
                .username(userCreateRequest.username())
                .build();
        userRepository.save(user);

        return user.getId();
    }

    public void changeUsername(long userId, String newUsername) {
        User user = findUserById(userId);

        user.setUsername(newUsername);
        userRepository.save(user);
    }

    public void changePassword(long userId, PasswordChangeRequest passwordChangeRequest) {
        User user = findUserById(userId);

        if (!passwordChangeRequest.isPasswordMatch()) throw new BadRequestException(ErrorCode.NOT_EQUALS_NEW_PASSWORD);
        if (!BCrypt.checkpw(passwordChangeRequest.oldPassword(), user.getPassword())) throw new BadRequestException(ErrorCode.NOT_EQUALS_OLD_PASSWORD);
        String hashedNewPassword = BCrypt.hashpw(passwordChangeRequest.newPassword(), BCrypt.gensalt());

        user.setPassword(hashedNewPassword);
        userRepository.save(user);
    }

    public WithdrawView withdrawUser(long userId) {
        User user = findUserById(userId);
        LocalDateTime deletedAt = user.withdraw();
        userRepository.save(user);
        return new WithdrawView(userId, deletedAt);
    }

    public void reactiveUser(long userId) {
        User user = findUserById(userId);
        user.reactive();
        userRepository.save(user);
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    }
}
