package com.depromeet.global.security.jwt.util;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String accessTokenSecret, String refreshTokenSecret, Long accessTokenExpirationTime, Long refreshTokenExpirationTime, String issuer) {

}
