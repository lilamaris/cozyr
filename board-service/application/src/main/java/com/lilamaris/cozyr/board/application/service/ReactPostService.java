package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.port.in.ReactPostUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.ReactPostCommand;
import com.lilamaris.cozyr.board.application.port.in.result.ReactedPostResult;
import com.lilamaris.cozyr.board.application.port.out.PostReactionReader;
import com.lilamaris.cozyr.board.application.port.out.PostReactionStore;
import com.lilamaris.cozyr.board.application.port.out.PostReader;
import com.lilamaris.cozyr.board.domain.PostReaction;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class ReactPostService implements ReactPostUseCase {
    private final PostReactionReader reader;
    private final PostReactionStore store;
    private final PostReader postReader;
    private final Clock clock;

    @Override
    @Transactional
    public ReactedPostResult react(ReactPostCommand command) {
        var postId = command.postId();
        if (!postReader.existsById(postId))
            throw new ApplicationException(BoardServiceProgressCode.POST_NOT_FOUND);

        var now = clock.instant();
        var userId = command.actorUserId();
        var reactionType = command.reactionType();

        if (reader.existsReaction(postId, userId, reactionType))
            throw new ApplicationException(BoardServiceProgressCode.REACTION_DUPLICATED);

        var postReaction = PostReaction.of(postId, userId, reactionType, now);
        var saved = store.save(postReaction);

        return ReactedPostResult.from(saved);
    }
}
