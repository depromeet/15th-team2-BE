package com.depromeet.global.security.jwt.util;

import com.depromeet.domain.member.domain.MemberRole;

public record AccessTokenDto(Long memberId, MemberRole memberRole) {

}
