package com.depromeet.domain.member.domain;

import com.depromeet.domain.member.dto.request.MemberCreateDto;
import com.depromeet.domain.member.dto.request.MemberUpdateDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {
    private Long id;
    private String name;
    private String email;
    private String password;
    private MemberRole role;
    private String refreshToken;

    @Builder
    public Member(Long id, String name, String email, String password, MemberRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Member from(MemberCreateDto memberCreate) {
        return Member.builder()
                .name(memberCreate.name())
                .email(memberCreate.email())
                .password(memberCreate.password())
                .role(MemberRole.USER)
                .build();
    }

    public Member update(MemberUpdateDto memberUpdate) {
        return Member.builder()
                .id(id)
                .name(memberUpdate.name() == null ? name : memberUpdate.name())
                .email(email)
                .password(memberUpdate.password() == null ? password : memberUpdate.password())
                .role(role)
                .build();
    }

    public void encodePassword(String password) {
        if(password != null) {
            this.password = password;
        }
    }

    public void updateRefreshToken(String refreshToken) {
        if(refreshToken != null) {
            this.refreshToken = refreshToken;
        }
    }
}
