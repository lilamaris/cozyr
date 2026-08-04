package com.lilamaris.cozyr.board.contract.event;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.message.MessageEnvelope;
import com.lilamaris.cozyr.kernel.message.MessageKind;
import com.lilamaris.cozyr.kernel.message.MessagePayload;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

public record CommentCreatedEvent(
        Long commentId,
        long postId,
        @Nullable Long parentId,
        UUID authorUserId,
        Instant createdAt
) implements MessagePayload {
    public CommentCreatedEvent {
        NumberPrecondition.requireNonNegative(commentId, "commentId");
        NumberPrecondition.requireNonNegative(postId, "postId");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        ObjectPrecondition.requireNonNull(authorUserId, "authorUserId");

        if (parentId != null) {
            NumberPrecondition.requireNonNegative(parentId, "parentId");
        }
    }

    public static CommentCreatedEvent of(Long commentId, long postId, @Nullable Long parentId, UUID authorUserId, Instant createdAt) {
        return new CommentCreatedEvent(commentId, postId, parentId, authorUserId, createdAt);
    }

    @Override
    public MessageKind kind() {
        return BoardServiceMessageKind.COMMENT_CREATED;
    }

    public MessageEnvelope<CommentCreatedEvent> toMessage(Instant now) {
        return MessageEnvelope.of(commentId.toString(), this, now);
    }
}
