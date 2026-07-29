package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.port.in.CreateCommentUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.CreateCommentCommand;
import com.lilamaris.cozyr.board.application.port.in.result.CreatedCommentResult;
import com.lilamaris.cozyr.board.application.port.out.CommentStore;
import com.lilamaris.cozyr.board.application.port.out.PostReader;
import com.lilamaris.cozyr.board.domain.Comment;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class CreateCommentService implements CreateCommentUseCase {
    private final PostReader postReader;
    private final CommentStore store;
    private final Clock clock;

    public CreateCommentService(PostReader postReader, CommentStore store, Clock clock) {
        this.postReader = postReader;
        this.store = store;
        this.clock = clock;
    }

    @Override
    @Transactional
    public CreatedCommentResult create(CreateCommentCommand command) {
        var now = clock.instant();
        var postId = command.postId();
        if (!postReader.existsById(postId))
            throw new ApplicationException(BoardServiceProgressCode.POST_NOT_FOUND);

        var content = command.content();
        var authorUserId = command.authorUserId();

        var comment = Comment.root(postId, content, now, authorUserId);
        var saved = store.save(comment);

        return CreatedCommentResult.of(saved);
    }
}
