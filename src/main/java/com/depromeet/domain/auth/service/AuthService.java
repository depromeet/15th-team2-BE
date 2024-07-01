package com.depromeet.domain.auth.service;

import com.depromeet.domain.auth.dto.request.LoginDto;
import com.depromeet.domain.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.domain.member.dto.request.MemberCreateDto;

public interface AuthService {
    JwtTokenResponseDto login(LoginDto loginDto);

    void signUp(MemberCreateDto memberCreateDto);
}
