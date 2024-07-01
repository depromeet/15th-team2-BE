package com.depromeet.global.security.jwt.util;

import com.depromeet.domain.member.domain.MemberRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final JwtProperties jwtProperties;

    public String generateAccessToken(Long memberId, MemberRole memberRole) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtProperties.accessTokenExpirationTime());


        return Jwts.builder()
                .issuer(jwtProperties.issuer())
                .subject(memberId.toString())
                .claim("role", memberRole.getValue())
                .issuedAt(now)
                .expiration(exp)
                .signWith(getJwtTokenKey(jwtProperties.accessTokenSecret()))
                .compact();

    }

    public String generateRefreshToken(Long memberId) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtProperties.refreshTokenExpirationTime());

        return Jwts.builder()
                .issuer(jwtProperties.issuer())
                .subject(memberId.toString())
                .issuedAt(now)
                .expiration(exp)
                .signWith(getJwtTokenKey(jwtProperties.refreshTokenSecret()))
                .compact();
    }

    public Optional<AccessTokenDto> parseAccessToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .requireIssuer(jwtProperties.issuer())
                    .verifyWith(getJwtTokenKey(jwtProperties.refreshTokenSecret()))
                    .build()
                    .parseSignedClaims(token);
            Long memberId = Long.valueOf(claims.getPayload().getSubject());
            MemberRole memberRole = MemberRole.findByValue(claims.getPayload().get("role").toString());
            return Optional.of(new AccessTokenDto(memberId, memberRole));
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<RefreshTokenDto> parseRefreshToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .requireIssuer(jwtProperties.issuer())
                    .verifyWith(getJwtTokenKey(jwtProperties.accessTokenSecret()))
                    .build()
                    .parseSignedClaims(token);
            Long memberId = Long.valueOf(claims.getPayload().getSubject());
            return Optional.of(new RefreshTokenDto(memberId));
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private SecretKey getJwtTokenKey(String tokenKey) {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(tokenKey.getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

}
