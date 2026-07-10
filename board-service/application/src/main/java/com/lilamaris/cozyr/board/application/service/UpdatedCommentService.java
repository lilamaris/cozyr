package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.port.in.UpdateCommentUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.UpdateCommentCommand;
import com.lilamaris.cozyr.board.application.port.in.result.UpdatedCommentResult;
import com.lilamaris.cozyr.board.application.port.out.CommentReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class UpdatedCommentService implements UpdateCommentUseCase {
    private final CommentReader reader;
    private final Clock clock;

    public UpdatedCommentService(CommentReader reader, Clock clock) {
        this.reader = reader;
        this.clock = clock;
    }

    @Override
    @Transactional
    public UpdatedCommentResult update(UpdateCommentCommand command) {
        var commentId = command.commentId();
        var comment = reader.findById(commentId)
                .orElseThrow(() -> new ApplicationException(BoardServiceProgressCode.COMMENT_NOT_FOUND));

        var now = clock.instant();
        var content = command.content();

        comment.updateContent(content, now);

        return UpdatedCommentResult.of(comment);
    }
}
