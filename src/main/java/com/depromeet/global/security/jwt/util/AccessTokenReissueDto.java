package com.depromeet.global.security.jwt.util;

import com.depromeet.domain.member.domain.MemberRole;

public record AccessTokenReissueDto(Long memberId, MemberRole memberRole, String accessToken) {
}
