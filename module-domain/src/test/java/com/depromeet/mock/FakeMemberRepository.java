package com.depromeet.mock;

import com.depromeet.member.domain.Member;
import com.depromeet.member.port.out.persistence.MemberPersistencePort;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeMemberRepository implements MemberPersistencePort {
    private Long autoGeneratedId = 0L;
    private final List<Member> data = new ArrayList<>();

    @Override
    public Optional<Member> findByEmail(String email) {
        return data.stream().filter(item -> item.getEmail().equals(email)).findAny();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return data.stream().filter(item -> item.getId().equals(id)).findAny();
    }

    @Override
    public Member save(Member member) {
        if (member.getId() == null || member.getId() == 0) {
            Member newMember =
                    Member.builder()
                            .id(++autoGeneratedId)
                            .name(member.getName())
                            .email(member.getEmail())
                            .role(member.getRole())
                            .build();
            data.add(newMember);
            return newMember;
        } else {
            data.removeIf(item -> item.getId().equals(member.getId()));
            data.add(member);
            return member;
        }
    }

    @Override
    public Optional<Member> updateGoal(Long memberId, Integer goal) {
        return findById(memberId)
                .map(
                        item -> {
                            Member member = item.updateGoal(goal);
                            save(member);
                            return member;
                        });
    }

    @Override
    public Optional<Member> updateName(Long memberId, String name) {
        return findById(memberId)
                .map(
                        item -> {
                            Member member = item.updateName(name);
                            save(member);
                            return member;
                        });
    }

    @Override
    public Optional<Member> findByProviderId(String providerId) {
        return data.stream().filter(member -> member.getProviderId().equals(providerId)).findAny();
    }
}
