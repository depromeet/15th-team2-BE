package com.depromeet.notification.repository;

import static com.depromeet.memory.entity.QMemoryEntity.*;
import static com.depromeet.notification.entity.QReactionLogEntity.*;
import static com.depromeet.reaction.entity.QReactionEntity.*;
import static com.querydsl.core.types.ExpressionUtils.*;

import com.depromeet.member.entity.QMemberEntity;
import com.depromeet.notification.domain.ReactionLog;
import com.depromeet.notification.entity.ReactionLogEntity;
import com.depromeet.notification.port.out.ReactionLogPersistencePort;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReactionLogRepository implements ReactionLogPersistencePort {
    private final JPAQueryFactory queryFactory;
    private final ReactionLogJpaRepository reactionLogJpaRepository;

    QMemberEntity member = new QMemberEntity("member");
    QMemberEntity memoryMember = new QMemberEntity("memoryMember");

    @Override
    public ReactionLog save(ReactionLog reactionLog) {
        return reactionLogJpaRepository
                .save(ReactionLogEntity.of(reactionLog.getReceiver(), reactionLog))
                .toPureModel();
    }

    @Override
    public List<ReactionLog> findByMemberIdAndCursorCreatedAt(
            Long memberId, LocalDateTime cursorCreatedAt) {
        return queryFactory
                .selectFrom(reactionLogEntity)
                .join(reactionLogEntity.reaction, reactionEntity)
                .fetchJoin()
                .join(reactionEntity.member, member)
                .fetchJoin()
                .join(reactionEntity.memory, memoryEntity)
                .fetchJoin()
                .where(memberEq(memberId), createdAtLoe(cursorCreatedAt))
                .limit(11)
                .orderBy(reactionLogEntity.createdAt.desc())
                .fetch()
                .stream()
                .map(ReactionLogEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void updateAllAsRead(Long memberId) {
        queryFactory
                .update(reactionLogEntity)
                .where(memberEq(memberId), reactionLogEntity.hasRead.isFalse())
                .set(reactionLogEntity.hasRead, true)
                .execute();
    }

    @Override
    public Long countUnread(Long memberId) {
        return queryFactory
                .select(count(reactionLogEntity))
                .from(reactionLogEntity)
                .join(reactionLogEntity.reaction, reactionEntity)
                .join(reactionEntity.member, member)
                .join(reactionEntity.memory, memoryEntity)
                .where(memberEq(memberId), reactionLogEntity.hasRead.isFalse())
                .fetchOne();
    }

    @Override
    public void deleteAllByReactionId(List<Long> reactionIds) {
        queryFactory.delete(reactionLogEntity).where(reactionIdIn(reactionIds)).execute();
    }

    private static BooleanExpression reactionIdIn(List<Long> reactionIds) {
        if (reactionIds == null) {
            return null;
        }
        return reactionLogEntity.reaction.id.in(reactionIds);
    }

    private ReactionLogEntity findByMemberIdAndLogId(Long memberId, Long reactionLogId) {
        return queryFactory
                .selectFrom(reactionLogEntity)
                .join(reactionLogEntity.reaction, reactionEntity)
                .fetchJoin()
                .join(reactionEntity.member, member)
                .fetchJoin()
                .join(reactionEntity.memory, memoryEntity)
                .fetchJoin()
                .where(memberEq(memberId), reactionLogEq(reactionLogId))
                .fetchOne();
    }

    private BooleanExpression createdAtLoe(LocalDateTime cursorCreatedAt) {
        if (cursorCreatedAt == null) {
            return null;
        }
        return reactionLogEntity.createdAt.loe(cursorCreatedAt);
    }

    private BooleanExpression memberEq(Long memberId) {
        if (memberId == null) {
            return null;
        }
        return reactionLogEntity.receiver.id.eq(memberId);
    }

    private BooleanExpression reactionLogEq(Long reactionLogId) {
        if (reactionLogId == null) {
            return null;
        }
        return reactionLogEntity.id.eq(reactionLogId);
    }
}
