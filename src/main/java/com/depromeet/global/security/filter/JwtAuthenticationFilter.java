package com.depromeet.global.security.filter;

import com.depromeet.domain.auth.service.JwtTokenService;
import com.depromeet.global.security.PrincipalDetails;
import com.depromeet.global.security.jwt.util.AccessTokenDto;
import com.depromeet.global.security.jwt.util.AccessTokenReissueDto;
import com.depromeet.global.security.jwt.util.RefreshTokenDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Optional;

import static com.depromeet.global.security.constant.SecurityConstant.ACCESS_HEADER;
import static com.depromeet.global.security.constant.SecurityConstant.REFRESH_HEADER;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> optionalAccessToken = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION));

        if (optionalAccessToken.isPresent()) {
            String accessToken = optionalAccessToken.get();
            Optional<AccessTokenDto> optionalAccessTokenDto = jwtTokenService.parseAccessToken(accessToken);

            if (optionalAccessTokenDto.isPresent()) {
                AccessTokenDto accessTokenDto = optionalAccessTokenDto.get();
                UserDetails userDetails = new PrincipalDetails(accessTokenDto.memberId(), accessTokenDto.memberRole());
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else {
                Optional<String> optionalRefreshToken = Optional.ofNullable(WebUtils.getCookie(request, "refresh"))
                        .map(Cookie::getValue);
                if(optionalRefreshToken.isPresent()) {
                    String refreshToken = optionalRefreshToken.get();
                    Optional<RefreshTokenDto> optionalRefreshTokenDto = jwtTokenService.parseRefreshToken(refreshToken);
                    if (optionalRefreshTokenDto.isPresent()) {
                        RefreshTokenDto refreshTokenDto = optionalRefreshTokenDto.get();
                        refreshTokenDto = jwtTokenService.retrieveRefreshToken(refreshTokenDto, refreshToken);
                        AccessTokenReissueDto reissuedAccessToken = jwtTokenService.reissueAccessToken(accessToken);
                        String reissuedRefreshToken = jwtTokenService.reissueRefreshToken(refreshToken);

                        response.setHeader(ACCESS_HEADER.getValue(), reissuedAccessToken.accessToken());
                        response.setHeader(REFRESH_HEADER.getValue(), reissuedRefreshToken);

                        UserDetails userDetails = new PrincipalDetails(reissuedAccessToken.memberId(), reissuedAccessToken.memberRole());
                        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                    }
                } else {
                    filterChain.doFilter(request, response);
                }

            }

        } else {
            filterChain.doFilter(request, response);
        }
    }
}
