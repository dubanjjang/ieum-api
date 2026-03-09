package com.dubanjang.ieum.user.presentation.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordChangeRequest(
        String oldPassword,

        @NotBlank(message = "변경할 비밀번호는 필수 입력 인자입니다.")
        @Size(min = 8, max = 29, message = "비밀번호는 8자 이상 30자 미만으로 입력해주세요.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]+$",
                message = "비밀번호는 영문, 숫자, 특수문자를 최소 하나씩 포함해야 합니다."
        )
        String newPassword,

        @NotBlank(message = "비밀번호 확인은 필수 입력 인자입니다.")
        String newConfirmedPassword
) {

    @AssertTrue(message = "비밀번호와 비밀번호 확인이 일치하지 않습니다.")
    public boolean isPasswordMatch() {
        return newPassword != null && newPassword.equals(newConfirmedPassword);
    }
}
