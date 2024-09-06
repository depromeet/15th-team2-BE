package com.depromeet.blacklist.port.out.persistence;

import com.depromeet.blacklist.domain.Blacklist;
import com.depromeet.member.domain.Member;
import java.util.List;

public interface BlacklistPersistencePort {
    Blacklist save(Blacklist blacklist);

    boolean existsByMemberIdAndBlackMemberId(Long memberId, Long blackMemberId);

    void unblackMember(Long memberId, Long blackMemberId);

    List<Member> findBlackMembers(Long memberId, Long cursorId);
}
