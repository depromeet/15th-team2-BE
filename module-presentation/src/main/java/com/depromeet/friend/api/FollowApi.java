package com.depromeet.friend.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.friend.dto.request.FollowingRequest;
import com.depromeet.friend.dto.response.FollowSliceResponse;
import com.depromeet.friend.dto.response.FollowerFollowingCountResponse;
import com.depromeet.friend.dto.response.FollowerResponse;
import com.depromeet.friend.dto.response.FollowingResponse;
import com.depromeet.member.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "팔로워/팔로잉(follower/following)")
public interface FollowApi {
    @Operation(summary = "팔로잉 추가")
    ApiResponse<?> addOrDeleteFollowing(
            @LoginMember Long memberId, @RequestBody FollowingRequest followingRequest);

    @Operation(summary = "팔로잉 리스트 조회")
    ApiResponse<FollowSliceResponse<FollowingResponse>> findFollowingList(
            @LoginMember Long memberId,
            @RequestParam(value = "cursorId", required = false) Long cursorId);

    @Operation(summary = "팔로워 리스트 조회")
    ApiResponse<FollowSliceResponse<FollowerResponse>> findFollowerList(
            @LoginMember Long memberId,
            @RequestParam(value = "cursorId", required = false) Long cursorId);

    @Operation(summary = "팔로워/팔로잉 숫자 조회")
    ApiResponse<FollowerFollowingCountResponse> getFollowerFollowingCount(
            @LoginMember Long memberId);
}
