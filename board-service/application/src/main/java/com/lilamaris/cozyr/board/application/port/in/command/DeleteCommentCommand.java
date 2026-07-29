package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.util.UUID;

public record DeleteCommentCommand(
        Long commentId,
        UUID actorUserId
) {
    public DeleteCommentCommand {
        NumberPrecondition.requireNonNegative(commentId, "commentId");
        ObjectPrecondition.requireNonNull(actorUserId, "actorUserId");
    }

    public static DeleteCommentCommand of(Long commentId, UUID actorUserId) {
        return new DeleteCommentCommand(commentId, actorUserId);
    }
}
