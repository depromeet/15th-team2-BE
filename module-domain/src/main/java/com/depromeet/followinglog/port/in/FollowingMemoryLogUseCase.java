package com.depromeet.followinglog.port.in;

import com.depromeet.followinglog.domain.vo.FollowingLogSlice;
import com.depromeet.followinglog.port.in.command.CreateFollowingMemoryCommand;
import java.util.List;

public interface FollowingMemoryLogUseCase {
    void save(CreateFollowingMemoryCommand followingMemoryCommand);

    FollowingLogSlice findLogsByMemberIdAndCursorId(Long memberId, Long cursorId);

    void deleteAllByMemoryIds(List<Long> memoryIds);

    void deleteAllByMemoryId(Long memoryId);
}
