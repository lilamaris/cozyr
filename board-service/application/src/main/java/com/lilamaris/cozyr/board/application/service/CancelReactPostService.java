package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.policy.PostReactionAccessPolicy;
import com.lilamaris.cozyr.board.application.port.in.CancelReactPostUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.CancelReactPostCommand;
import com.lilamaris.cozyr.board.application.port.out.PostReactionReader;
import com.lilamaris.cozyr.board.application.port.out.PostReactionStore;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CancelReactPostService implements CancelReactPostUseCase {
    private final PostReactionReader reader;
    private final PostReactionStore store;
    private final PostReactionAccessPolicy policy;

    @Override
    @Transactional
    public void cancel(CancelReactPostCommand command) {
        var reaction = reader.findById(command.reactionId())
                .orElseThrow(() -> new ApplicationException(BoardServiceProgressCode.REACTION_NOT_FOUND));

        var userId = command.actorUserId();
        if (!policy.canDelete(reaction, userId))
            throw new ApplicationException(BoardServiceProgressCode.REACTION_DELETE_ACCESS_DENIED);

        store.delete(reaction);
    }
}
