package com.depromeet.auth.dto.request;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

// 임시
@Deprecated
@Hidden
@Schema(name = "login")
public record LoginDto(
        @Schema(defaultValue = "user@gmail.com") String email,
        @Schema(defaultValue = "password") String password) {
    public LoginDto {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
    }
}
