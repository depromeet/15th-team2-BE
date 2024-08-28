package com.depromeet.friend.dto.response;

import com.depromeet.friend.domain.vo.FollowSlice;
import com.depromeet.friend.domain.vo.Follower;
import com.depromeet.friend.domain.vo.Following;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FollowSliceResponse<T>(
        @Schema(description = "팔로워/팔로잉 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
                List<T> contents,
        @Schema(description = "페이지 크기", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
                int pageSize,
        @Schema(
                        description = "다음 페이지 시작 ID",
                        example = "1",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                Long cursorId,
        @Schema(
                        description = "다음 페이지 유무",
                        example = "false",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                boolean hasNext) {
    @Builder
    public FollowSliceResponse {}

    public static FollowSliceResponse<FollowingResponse> toFollowingSliceResponse(
            FollowSlice<Following> followingSlice, String profileImageDomain) {
        List<FollowingResponse> followingResponses =
                getFollowingResponses(followingSlice, profileImageDomain);
        return FollowSliceResponse.<FollowingResponse>builder()
                .contents(followingResponses)
                .pageSize(followingSlice.getPageSize())
                .cursorId(followingSlice.getCursorId())
                .hasNext(followingSlice.isHasNext())
                .build();
    }

    public static FollowSliceResponse<FollowerResponse> toFollowerSliceResponses(
            FollowSlice<Follower> followingSlice, String profileImageOrigin) {
        List<FollowerResponse> followingResponses =
                getFollowerResponses(followingSlice, profileImageOrigin);
        return FollowSliceResponse.<FollowerResponse>builder()
                .contents(followingResponses)
                .pageSize(followingSlice.getPageSize())
                .cursorId(followingSlice.getCursorId())
                .hasNext(followingSlice.isHasNext())
                .build();
    }

    private static List<FollowingResponse> getFollowingResponses(
            FollowSlice<Following> followingSlice, String profileImageOrigin) {
        return followingSlice.getFollowContents().stream()
                .map(
                        following ->
                                FollowingResponse.builder()
                                        .memberId(following.getMemberId())
                                        .nickname(following.getName())
                                        .profileImageUrl(
                                                following.getProfileImageUrl(profileImageOrigin))
                                        .introduction(following.getIntroduction())
                                        .build())
                .toList();
    }

    private static List<FollowerResponse> getFollowerResponses(
            FollowSlice<Follower> followingSlice, String profileImageOrigin) {
        return followingSlice.getFollowContents().stream()
                .map(
                        follower ->
                                FollowerResponse.builder()
                                        .memberId(follower.getMemberId())
                                        .nickname(follower.getName())
                                        .profileImageUrl(
                                                follower.getProfileImageUrl(profileImageOrigin))
                                        .introduction(follower.getIntroduction())
                                        .hasFollowedBack(follower.isHasFollowedBack())
                                        .build())
                .toList();
    }
}
