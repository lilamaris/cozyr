package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.policy.PostAccessPolicy;
import com.lilamaris.cozyr.board.application.port.in.DeletePostUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.DeletePostCommand;
import com.lilamaris.cozyr.board.application.port.out.PostReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class DeletePostService implements DeletePostUseCase {
    private final PostReader reader;
    private final PostAccessPolicy policy;
    private final Clock clock;

    public DeletePostService(PostReader reader, PostAccessPolicy policy, Clock clock) {
        this.reader = reader;
        this.policy = policy;
        this.clock = clock;
    }

    @Override
    @Transactional
    public void delete(DeletePostCommand command) {
        var postId = command.postId();
        var post = reader.findById(postId)
                .orElseThrow(() -> new ApplicationException(BoardServiceProgressCode.POST_NOT_FOUND));

        var actorUserId = command.actorUserId();
        if (!policy.canDelete(post, actorUserId))
            throw new ApplicationException(BoardServiceProgressCode.POST_DELETE_ACCESS_DENIED);

        var now = clock.instant();
        post.delete(now);
    }
}
