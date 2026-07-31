package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.util.UUID;

public record CancelReactPostCommand(
        UUID reactionId,
        UUID actorUserId
) {
    public CancelReactPostCommand {
        ObjectPrecondition.requireNonNull(reactionId, "reactionId");
        ObjectPrecondition.requireNonNull(actorUserId, "actorUserId");
    }

    public static CancelReactPostCommand of(UUID reactionId, UUID actorUserId) {
        return new CancelReactPostCommand(reactionId, actorUserId);
    }
}
