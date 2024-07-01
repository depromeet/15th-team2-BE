package com.depromeet.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;
//임시
@Schema(name = "회원가입 정보 입력")
public record MemberCreateDto(@Schema(defaultValue = "user") String name,
                              @Schema(defaultValue = "user@gmail.com") String email,
                              @Schema(defaultValue = "password") String password) {

    public MemberCreateDto {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
    }
}
