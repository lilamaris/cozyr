package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.port.in.ReplyCommentUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.ReplyCommentCommand;
import com.lilamaris.cozyr.board.application.port.in.result.RepliedCommentResult;
import com.lilamaris.cozyr.board.application.port.out.CommentReader;
import com.lilamaris.cozyr.board.application.port.out.CommentStore;
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
public class ReplyCommentService implements ReplyCommentUseCase {
    private final CommentReader reader;
    private final CommentStore store;
    private final MessagePublisher messagePublisher;
    private final Clock clock;

    @Override
    @Transactional
    public RepliedCommentResult reply(ReplyCommentCommand command) {
        var parentId = command.parentId();
        var parent = reader.findById(parentId)
                .orElseThrow(() -> new ApplicationException(BoardServiceProgressCode.COMMENT_NOT_FOUND));

        var now = clock.instant();
        var content = command.content();
        var authorUserId = command.authorUserId();
        var comment = Comment.reply(parent, content, now, authorUserId);
        var saved = store.save(comment);

        var event = CommentCreatedEvent.of(saved.getId(), saved.getPostId(), saved.getParentId(), now);
        messagePublisher.publish(event.toMessage(now));

        return RepliedCommentResult.of(saved);
    }
}
