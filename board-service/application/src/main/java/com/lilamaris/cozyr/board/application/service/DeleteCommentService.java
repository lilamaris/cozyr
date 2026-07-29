package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.policy.CommentAccessPolicy;
import com.lilamaris.cozyr.board.application.port.in.DeleteCommentUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.DeleteCommentCommand;
import com.lilamaris.cozyr.board.application.port.out.CommentReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class DeleteCommentService implements DeleteCommentUseCase {
    private final CommentReader reader;
    private final CommentAccessPolicy policy;
    private final Clock clock;

    public DeleteCommentService(CommentReader reader, CommentAccessPolicy policy, Clock clock) {
        this.reader = reader;
        this.policy = policy;
        this.clock = clock;
    }

    @Override
    @Transactional
    public void delete(DeleteCommentCommand command) {
        var commentId = command.commentId();
        var comment = reader.findById(commentId)
                .orElseThrow(() -> new ApplicationException(BoardServiceProgressCode.COMMENT_NOT_FOUND));

        var actorUserId = command.actorUserId();
        if (!policy.canDelete(comment, actorUserId))
            throw new ApplicationException(BoardServiceProgressCode.COMMENT_DELETE_ACCESS_DENIED);

        var now = clock.instant();
        comment.delete(now);
    }
}
