package com.depromeet.domain.auth.service;

import com.depromeet.domain.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.domain.auth.exception.AuthException;
import com.depromeet.domain.member.domain.Member;
import com.depromeet.domain.member.domain.MemberRole;
import com.depromeet.domain.member.exception.MemberException;
import com.depromeet.domain.member.service.port.MemberRepository;
import com.depromeet.global.security.jwt.util.AccessTokenDto;
import com.depromeet.global.security.jwt.util.AccessTokenReissueDto;
import com.depromeet.global.security.jwt.util.JwtUtils;
import com.depromeet.global.security.jwt.util.RefreshTokenDto;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.depromeet.domain.auth.exception.AuthErrorCode.REFRESH_TOKEN_NOT_MATCH;
import static com.depromeet.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Transactional
@Service
public class JwtTokenService {
    private final JwtUtils jwtUtils;
    private final MemberRepository memberRepository;

    public JwtTokenResponseDto generateToken(Long memberId, MemberRole memberRole) {
        String accessToken = jwtUtils.generateAccessToken(memberId, memberRole);
        String refreshToken = jwtUtils.generateRefreshToken(memberId);

        return new JwtTokenResponseDto("Bearer " + accessToken, "Bearer " + refreshToken);
    }

    public Optional<AccessTokenDto> parseAccessToken(String token) {
        return jwtUtils.parseAccessToken(token);
    }

    public Optional<RefreshTokenDto> parseRefreshToken(String token) {
        return jwtUtils.parseRefreshToken(token);
    }

    public AccessTokenReissueDto reissueAccessToken(String token) {
        try {
            parseAccessToken(token);
            return null;
        } catch (ExpiredJwtException e) {
            Long memberId = Long.parseLong(e.getClaims().getSubject());

            String refreshToken = jwtUtils.generateRefreshToken(memberId);
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
            member.updateRefreshToken(refreshToken);
            memberRepository.save(member);

            return new AccessTokenReissueDto(memberId, member.getRole(), refreshToken);
        }
    }

    public String reissueRefreshToken(String token) {
        try {
            parseRefreshToken(token);
            return null;
        } catch (ExpiredJwtException e) {
            Long memberId = Long.parseLong(e.getClaims().getSubject());

            return jwtUtils.generateRefreshToken(memberId);
        }
    }

    public RefreshTokenDto retrieveRefreshToken(RefreshTokenDto refreshTokenDto, String refreshToken) {
        Member member = memberRepository.findById(refreshTokenDto.memberId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        if(member.getRefreshToken().equals(refreshToken)) {
            return refreshTokenDto;
        }
        throw new AuthException(REFRESH_TOKEN_NOT_MATCH);
    }
}
