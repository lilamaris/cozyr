package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.util.UUID;

public record DeletePostCommand(
        Long postId,
        UUID actorUserId
) {
    public DeletePostCommand {
        NumberPrecondition.requireNonNegative(postId, "postId");
        ObjectPrecondition.requireNonNull(actorUserId, "actorUserId");
    }

    public static DeletePostCommand of(Long postId, UUID actorUserId) {
        return new DeletePostCommand(postId, actorUserId);
    }
}
