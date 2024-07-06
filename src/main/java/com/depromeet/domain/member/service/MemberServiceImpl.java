package com.depromeet.domain.member.service;

import com.depromeet.domain.member.controller.port.MemberService;
import com.depromeet.domain.member.domain.Member;
import com.depromeet.domain.member.dto.request.MemberCreateDto;
import com.depromeet.domain.member.dto.response.MemberFindOneResponseDto;
import com.depromeet.domain.member.service.port.MemberRepository;
import com.depromeet.global.dto.type.member.MemberErrorType;
import com.depromeet.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member save(MemberCreateDto memberCreate) {
        Member member = Member.from(memberCreate);
        member.encodePassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }

    @Override
    public MemberFindOneResponseDto findOneMemberResponseById(Long id) {
        Member member =
                memberRepository
                        .findById(id)
                        .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
        return MemberFindOneResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }

    @Override
    public Member findById(Long id) {
        return memberRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
    }

    @Override
    public boolean matchPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
