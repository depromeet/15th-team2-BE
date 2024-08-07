package com.depromeet.oauth.dto;

import com.depromeet.member.domain.MemberRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDto {
    private Long id;
    private String nickname;
    private String email;
    private MemberRole memberRole;
    private String providerId;
}
