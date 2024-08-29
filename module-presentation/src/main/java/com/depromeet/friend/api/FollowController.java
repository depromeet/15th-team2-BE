package com.depromeet.friend.api;

import com.depromeet.config.Logging;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.friend.dto.request.FollowCheckListRequest;
import com.depromeet.friend.dto.request.FollowRequest;
import com.depromeet.friend.dto.response.*;
import com.depromeet.friend.facade.FollowFacade;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.type.friend.FollowSuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FollowController implements FollowApi {
    private final FollowFacade followFacade;

    @PutMapping
    @Logging(item = "Follower/Following", action = "PUT")
    public ApiResponse<?> addOrDeleteFollow(
            @LoginMember Long memberId, @RequestBody FollowRequest followRequest) {
        boolean hasFollowAdded = followFacade.addOrDeleteFollow(memberId, followRequest);

        if (hasFollowAdded) {
            return ApiResponse.success(FollowSuccessType.ADD_FOLLOWING_SUCCESS);
        }
        return ApiResponse.success(FollowSuccessType.DELETE_FOLLOWING_SUCCESS);
    }

    @GetMapping("/{memberId}/following")
    @Logging(item = "Follower/Following", action = "GET")
    public ApiResponse<FollowSliceResponse<FollowingResponse>> findFollowingList(
            @PathVariable(value = "memberId") Long memberId,
            @RequestParam(value = "cursorId", required = false) Long cursorId) {
        FollowSliceResponse<FollowingResponse> response =
                followFacade.findFollowingList(memberId, cursorId);
        return ApiResponse.success(FollowSuccessType.GET_FOLLOWINGS_SUCCESS, response);
    }

    @GetMapping("/{memberId}/follower")
    @Logging(item = "Follower/Following", action = "GET")
    public ApiResponse<FollowSliceResponse<FollowerResponse>> findFollowerList(
            @PathVariable(value = "memberId") Long memberId,
            @RequestParam(value = "cursorId", required = false) Long cursorId) {
        FollowSliceResponse<FollowerResponse> response =
                followFacade.findFollowerList(memberId, cursorId);
        return ApiResponse.success(FollowSuccessType.GET_FOLLOWERS_SUCCESS, response);
    }

    @GetMapping("/following/summary")
    @Logging(item = "Follower/Following", action = "GET")
    public ApiResponse<FollowingSummaryResponse> findFollowingSummary(@LoginMember Long memberId) {
        FollowingSummaryResponse response = followFacade.findFollowingSummary(memberId);
        return ApiResponse.success(FollowSuccessType.GET_FOLLOWING_SUMMARY_SUCCESS, response);
    }

    @PostMapping
    @Logging(item = "Follower/Following", action = "GET")
    public ApiResponse<IsFollowingResponse> checkFollowing(
            @LoginMember Long memberId, @RequestBody FollowCheckListRequest targetMemberId) {
        IsFollowingResponse response = followFacade.isFollowing(memberId, targetMemberId);
        return ApiResponse.success(FollowSuccessType.CHECK_FOLLOWING_SUCCESS, response);
    }
}
