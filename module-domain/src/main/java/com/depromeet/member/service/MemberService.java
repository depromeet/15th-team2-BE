package com.depromeet.member.service;

import com.depromeet.auth.port.out.persistence.RefreshRedisPersistencePort;
import com.depromeet.exception.BadRequestException;
import com.depromeet.exception.InternalServerException;
import com.depromeet.exception.NotFoundException;
import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberGender;
import com.depromeet.member.domain.MemberRole;
import com.depromeet.member.domain.vo.MemberIdAndNickname;
import com.depromeet.member.domain.vo.MemberSearchInfo;
import com.depromeet.member.domain.vo.MemberSearchPage;
import com.depromeet.member.port.in.command.SocialMemberCommand;
import com.depromeet.member.port.in.command.UpdateMemberCommand;
import com.depromeet.member.port.in.usecase.GoalUpdateUseCase;
import com.depromeet.member.port.in.usecase.MemberUpdateUseCase;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import com.depromeet.member.port.out.persistence.MemberPersistencePort;
import com.depromeet.type.member.MemberErrorType;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Profile("!batch")
@RequiredArgsConstructor
public class MemberService implements MemberUseCase, GoalUpdateUseCase, MemberUpdateUseCase {
    private final MemberPersistencePort memberPersistencePort;
    private final RefreshRedisPersistencePort refreshRedisPersistencePort;

    @Override
    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberPersistencePort
                .findById(id)
                .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
    }

    @Override
    public Member findOrCreateMemberBy(SocialMemberCommand command) {
        return memberPersistencePort
                .findByProviderId(command.providerId())
                .orElseGet(
                        () -> {
                            Member member =
                                    Member.builder()
                                            .nickname(command.name())
                                            .email(command.email())
                                            .role(MemberRole.USER)
                                            .providerId(command.providerId())
                                            .build();
                            return memberPersistencePort.save(member);
                        });
    }

    @Override
    public void deleteById(Long id) {
        memberPersistencePort.deleteById(id);
        refreshRedisPersistencePort.deleteData(id);
    }

    @Override
    public MemberSearchPage searchMemberByName(Long memberId, String nameQuery, Long cursorId) {
        List<MemberSearchInfo> memberSearchInfos =
                memberPersistencePort.searchByNameQuery(memberId, nameQuery, cursorId);

        boolean hasNext = false;
        Long nextCursorId = null;
        if (memberSearchInfos.size() > 10) {
            memberSearchInfos = new ArrayList<>(memberSearchInfos);
            memberSearchInfos.removeLast();
            hasNext = true;
            nextCursorId = memberSearchInfos.getLast().getMemberId();
        }
        return MemberSearchPage.builder()
                .members(memberSearchInfos)
                .cursorId(nextCursorId)
                .hasNext(hasNext)
                .build();
    }

    @Override
    public Member findByProviderId(String providerId) {
        return memberPersistencePort.findByProviderId(providerId).orElse(null);
    }

    @Override
    public Member createMemberBy(SocialMemberCommand command) {
        Member member =
                Member.builder()
                        .nickname(command.name())
                        .email(command.email())
                        .role(MemberRole.USER)
                        .providerId(command.providerId())
                        .profileImageUrl(command.defaultProfile())
                        .build();
        return memberPersistencePort.save(member);
    }

    @Override
    public MemberIdAndNickname findIdAndNicknameById(Long memberId) {
        return memberPersistencePort
                .findIdAndNicknameById(memberId)
                .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
    }

    @Override
    public Member update(UpdateMemberCommand command) {
        if (command.nickname() != null && command.nickname().isBlank()) {
            throw new BadRequestException(MemberErrorType.NAME_CANNOT_BE_BLANK);
        }
        return memberPersistencePort
                .update(command)
                .orElseThrow(() -> new InternalServerException(MemberErrorType.UPDATE_FAILED));
    }

    @Override
    public Member updateGoal(Long memberId, Integer goal) {
        return memberPersistencePort
                .updateGoal(memberId, goal)
                .orElseThrow(() -> new InternalServerException(MemberErrorType.UPDATE_GOAL_FAILED));
    }

    @Override
    public Member updateNickname(Long memberId, String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new BadRequestException(MemberErrorType.NAME_CANNOT_BE_BLANK);
        }
        return memberPersistencePort
                .updateNickname(memberId, nickname)
                .orElseThrow(() -> new InternalServerException(MemberErrorType.UPDATE_NAME_FAILED));
    }

    @Override
    public Member updateGender(Long memberId, MemberGender gender) {
        if (gender == null) {
            throw new BadRequestException(MemberErrorType.GENDER_CANNOT_BE_BLANK);
        }
        return memberPersistencePort
                .updateGender(memberId, gender)
                .orElseThrow(
                        () -> new InternalServerException(MemberErrorType.UPDATE_GENDER_FAILED));
    }

    @Override
    public Member updateLatestViewedFollowingLogAt(Long memberId) {
        return memberPersistencePort
                .updateLatestViewedFollowingLogAt(memberId)
                .orElseThrow(
                        () ->
                                new InternalServerException(
                                        MemberErrorType.UPDATE_LAST_VIEWED_FOLLOWING_LOG_AT));
    }

    @Override
    public Member updateProfileImageUrl(Long memberId, String profileImageUrl) {
        return memberPersistencePort
                .updateProfileImageUrl(memberId, profileImageUrl)
                .orElseThrow(
                        () ->
                                new InternalServerException(
                                        MemberErrorType.UPDATE_PROFILE_IMAGE_FAILED));
    }
}
