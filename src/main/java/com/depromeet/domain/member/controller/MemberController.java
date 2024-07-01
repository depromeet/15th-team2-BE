package com.depromeet.domain.member.controller;

import com.depromeet.domain.member.controller.port.MemberService;
import com.depromeet.domain.member.dto.response.MemberFindOneResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자(members)")
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "id로 member 단일 검색")
    @GetMapping("/{id}")
    public MemberFindOneResponseDto getMember(@PathVariable Long id) {
        return memberService.findOneMemberResponseById(id);
    }
}
