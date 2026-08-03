package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.port.in.CreateCommentUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.CreateCommentCommand;
import com.lilamaris.cozyr.board.application.port.in.result.CreatedCommentResult;
import com.lilamaris.cozyr.board.application.port.out.CommentStore;
import com.lilamaris.cozyr.board.application.port.out.PostReader;
import com.lilamaris.cozyr.board.contract.event.CommentCreatedEvent;
import com.lilamaris.cozyr.board.domain.Comment;
import com.lilamaris.cozyr.kernel.message.MessagePublisher;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class CreateCommentService implements CreateCommentUseCase {
    private final PostReader postReader;
    private final CommentStore store;
    private final MessagePublisher messagePublisher;
    private final Clock clock;

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

        var event = CommentCreatedEvent.of(saved.getId(), saved.getPostId(), saved.getParentId(), now);
        messagePublisher.publish(event.toMessage(now));

        return CreatedCommentResult.of(saved);
    }
}
