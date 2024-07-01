package com.depromeet.domain.member.controller.port;

import com.depromeet.domain.member.domain.Member;
import com.depromeet.domain.member.dto.request.MemberCreateDto;
import com.depromeet.domain.member.dto.response.MemberFindOneResponseDto;

public interface MemberService {
    Member save(MemberCreateDto memberCreate);

    MemberFindOneResponseDto findOneMemberResponseById(Long id);

    Member findById(Long id);

    Member findByEmail(String email);

    boolean matchPassword(String rawPassword, String encodedPassword);
}
