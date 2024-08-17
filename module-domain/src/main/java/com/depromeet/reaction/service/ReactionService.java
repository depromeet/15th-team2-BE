package com.depromeet.reaction.service;

import com.depromeet.exception.BadRequestException;
import com.depromeet.exception.ForbiddenException;
import com.depromeet.exception.NotFoundException;
import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;
import com.depromeet.reaction.domain.Reaction;
import com.depromeet.reaction.domain.ReactionPage;
import com.depromeet.reaction.port.in.command.CreateReactionCommand;
import com.depromeet.reaction.port.in.usecase.CreateReactionUseCase;
import com.depromeet.reaction.port.in.usecase.DeleteReactionUseCase;
import com.depromeet.reaction.port.in.usecase.GetReactionUseCase;
import com.depromeet.reaction.port.out.persistence.ReactionPersistencePort;
import com.depromeet.type.reaction.ReactionErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReactionService
        implements CreateReactionUseCase, GetReactionUseCase, DeleteReactionUseCase {
    private final ReactionPersistencePort reactionPersistencePort;
    private static final int MAXIMUM_REACTION_NUMBER = 3;
    private static final int REACTION_PAGE_SIZE = 10;

    @Override
    @Transactional
    public Reaction save(Long memberId, Memory memory, CreateReactionCommand command) {
        if (isOwnMemory(memberId, memory)) {
            throw new BadRequestException(ReactionErrorType.SAME_USER);
        }

        List<Reaction> reactions = getAllByMemberAndMemory(memberId, memory.getId());
        if (isOverMaximumCreationLimit(reactions)) {
            throw new BadRequestException(ReactionErrorType.MAXIMUM_FAILED);
        }

        Reaction reaction =
                Reaction.builder()
                        .member(Member.builder().id(memberId).build())
                        .memory(memory)
                        .emoji(command.emoji())
                        .comment(command.comment())
                        .build();

        return reactionPersistencePort.save(reaction);
    }

    @Override
    @Transactional
    public void deleteById(Long memberId, Long reactionId) {
        Reaction reaction = getReactionById(reactionId);

        if (!isOwnMemory(memberId, reaction.getMemory())) {
            throw new ForbiddenException(ReactionErrorType.FORBIDDEN_DELETE);
        }

        reactionPersistencePort.deleteById(reactionId);
    }

    @Override
    public List<Reaction> getReactionsOfMemory(Long memoryId) {
        return reactionPersistencePort.getAllByMemoryId(memoryId);
    }

    @Override
    public ReactionPage getDetailReactions(Long memberId, Long memoryId, Long cursorId) {
        List<Reaction> reactionDomains =
                reactionPersistencePort.getPagingReactions(memoryId, cursorId);

        if (isNotMyMemory(memberId, reactionDomains)) {
            throw new ForbiddenException(ReactionErrorType.FORBIDDEN_READ);
        }

        boolean hasNext = reactionDomains.size() > REACTION_PAGE_SIZE;
        Long nextCursorId = null;
        if (hasNext) {
            Reaction lastReaction = reactionDomains.removeLast();
            nextCursorId = lastReaction.getId();
        }

        return ReactionPage.of(reactionDomains, nextCursorId, hasNext);
    }

    @Override
    public Long getDetailReactionsCount(Long memoryId) {
        return reactionPersistencePort.getAllCountByMemoryId(memoryId);
    }

    private boolean isOverMaximumCreationLimit(List<Reaction> reactions) {
        return !reactions.isEmpty() && reactions.size() >= MAXIMUM_REACTION_NUMBER;
    }

    private List<Reaction> getAllByMemberAndMemory(Long memberId, Long memoryId) {
        return reactionPersistencePort.getAllByMemberAndMemory(memberId, memoryId);
    }

    private boolean isOwnMemory(Long memberId, Memory memory) {
        return memberId.equals(memory.getMember().getId());
    }

    private boolean isNotMyMemory(Long memberId, List<Reaction> reactionDomains) {
        return !reactionDomains.isEmpty()
                && !reactionDomains.getFirst().getMemory().getMember().getId().equals(memberId);
    }

    private Reaction getReactionById(Long reactionId) {
        return reactionPersistencePort
                .getReactionById(reactionId)
                .orElseThrow(() -> new NotFoundException(ReactionErrorType.NOT_FOUND));
    }
}
