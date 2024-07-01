package com.depromeet.domain.auth.controller;

import com.depromeet.domain.auth.dto.request.LoginDto;
import com.depromeet.domain.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.domain.auth.service.AuthService;
import com.depromeet.domain.member.controller.port.MemberService;
import com.depromeet.domain.member.dto.request.MemberCreateDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

import static com.depromeet.global.security.constant.SecurityConstant.ACCESS_HEADER;
import static com.depromeet.global.security.constant.SecurityConstant.REFRESH_HEADER;

@Tag(name = "로그인/회원가입")
@RequestMapping("/api/v1/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // 임시
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        JwtTokenResponseDto jwtTokenResponseDto = authService.login(loginDto);

        HttpHeaders httpHeaders =  new HttpHeaders();
        httpHeaders.add(REFRESH_HEADER.getValue(), jwtTokenResponseDto.refreshToken());
        httpHeaders.add(ACCESS_HEADER.getValue(), jwtTokenResponseDto.accessToken());

        return ResponseEntity.ok(httpHeaders);
    }

    @PostMapping
    public ResponseEntity<?> signUp(@RequestBody MemberCreateDto memberCreate) {
        authService.signUp(memberCreate);

        return ResponseEntity.ok().build();
    }
}
