package com.lilamaris.cozyr.board.contract.event;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import com.lilamaris.cozyr.kernel.message.MessageEnvelope;
import com.lilamaris.cozyr.kernel.message.MessageKind;
import com.lilamaris.cozyr.kernel.message.MessagePayload;

import java.time.Instant;
import java.util.UUID;

public record PostCreatedEvent(
        Long postId,
        UUID boardId,
        String title,
        UUID authorUserId,
        Instant createdAt
) implements MessagePayload {
    public PostCreatedEvent {
        NumberPrecondition.requireNonNegative(postId, "postId");
        ObjectPrecondition.requireNonNull(boardId, "boardId");
        StringPrecondition.requireNonBlank(title, "title");
        ObjectPrecondition.requireNonNull(authorUserId, "authorUserId");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
    }

    public static PostCreatedEvent of(Long postId, UUID boardId, String title, UUID authorUserId, Instant createdAt) {
        return new PostCreatedEvent(postId, boardId, title, authorUserId, createdAt);
    }

    @Override
    public MessageKind kind() {
        return BoardServiceMessageKind.POST_CREATED;
    }

    public MessageEnvelope<PostCreatedEvent> toMessage(Instant now) {
        return MessageEnvelope.of(postId.toString(), this, now);
    }
}
