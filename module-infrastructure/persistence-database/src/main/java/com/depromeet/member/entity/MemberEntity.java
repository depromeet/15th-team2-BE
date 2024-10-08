package com.depromeet.member.entity;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberGender;
import com.depromeet.member.domain.MemberRole;
import com.depromeet.member.port.in.command.UpdateMemberCommand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column private String nickname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column private MemberRole role;

    @Column private String providerId;

    @Column private Integer goal;

    @Column(columnDefinition = "char", nullable = false)
    private MemberGender gender;

    @Column private String profileImageUrl;

    @Column private String introduction;

    @Column private LocalDateTime lastViewedFollowingLogAt;

    @Builder
    public MemberEntity(
            Long id,
            String nickname,
            String email,
            MemberRole role,
            String providerId,
            Integer goal,
            MemberGender gender,
            String profileImageUrl,
            String introduction,
            LocalDateTime lastViewedFollowingLogAt) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.providerId = providerId;
        this.goal = goal;
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
        this.introduction = introduction;
        this.lastViewedFollowingLogAt = lastViewedFollowingLogAt;
    }

    public MemberEntity(Long id, String nickname, String profileImageUrl, String introduction) {
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.introduction = introduction;
    }

    @PrePersist
    public void prePersist() {
        this.goal = 1000;
        this.gender = MemberGender.W;
    }

    public static MemberEntity from(Member member) {
        return builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .role(member.getRole())
                .providerId(member.getProviderId())
                .goal(member.getGoal())
                .gender(member.getGender())
                .profileImageUrl(member.getProfileImageUrl())
                .introduction(member.getIntroduction())
                .lastViewedFollowingLogAt(member.getLastViewedFollowingLogAt())
                .build();
    }

    public Member toModel() {
        return Member.builder()
                .id(id)
                .nickname(nickname)
                .email(email)
                .role(role)
                .providerId(providerId)
                .goal(goal)
                .gender(gender)
                .profileImageUrl(profileImageUrl)
                .introduction(introduction)
                .lastViewedFollowingLogAt(lastViewedFollowingLogAt)
                .build();
    }

    public MemberEntity updateGoal(Integer goal) {
        this.goal = goal;
        return this;
    }

    public MemberEntity updateNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public MemberEntity updateGender(MemberGender gender) {
        this.gender = gender;
        return this;
    }

    public MemberEntity updateLastViewedFollowingLogAt() {
        this.lastViewedFollowingLogAt = LocalDateTime.now();
        return this;
    }

    public MemberEntity update(UpdateMemberCommand command) {
        if (command.nickname() != null && !command.nickname().isBlank()) {
            this.nickname = command.nickname();
        }
        if (command.introduction() != null) {
            this.introduction = command.introduction();
        }
        return this;
    }

    public MemberEntity updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        return this;
    }
}
