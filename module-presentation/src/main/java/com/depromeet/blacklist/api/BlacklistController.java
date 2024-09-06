package com.depromeet.blacklist.api;

import com.depromeet.blacklist.dto.request.BlackMemberRequest;
import com.depromeet.blacklist.facade.BlacklistFacade;
import com.depromeet.config.Logging;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.type.blacklist.BlacklistSuccessType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class BlacklistController implements BlacklistApi {
    private final BlacklistFacade blacklistFacade;

    @PostMapping("/black")
    @Logging(item = "Blacklist", action = "POST")
    public ApiResponse<?> blackMember(
            @LoginMember Long memberId, @Valid @RequestBody BlackMemberRequest request) {
        blacklistFacade.blackMember(memberId, request);
        return ApiResponse.success(BlacklistSuccessType.BLACK_MEMBER_SUCCESS);
    }

    @DeleteMapping("/{blackMemberId}/black")
    @Logging(item = "Blacklist", action = "DELETE")
    public ApiResponse<?> unblackMember(
            @LoginMember Long memberId, @PathVariable("blackMemberId") Long blackMemberId) {
        blacklistFacade.unblackMember(memberId, blackMemberId);
        return ApiResponse.success(BlacklistSuccessType.UNBLACK_MEMBER_SUCCESS);
    }
}