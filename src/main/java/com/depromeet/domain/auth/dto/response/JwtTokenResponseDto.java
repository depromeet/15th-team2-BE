package com.depromeet.domain.auth.dto.response;

import java.util.Objects;

public record JwtTokenResponseDto(String accessToken, String refreshToken) {
    public JwtTokenResponseDto {
        Objects.requireNonNull(accessToken);
        Objects.requireNonNull(refreshToken);
    }
}
