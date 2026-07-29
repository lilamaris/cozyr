package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.UUID;

public record UpdateCommentCommand(
        Long commentId,
        String content,
        UUID actorUserId
) {
    public UpdateCommentCommand {
        NumberPrecondition.requireNonNegative(commentId, "commentId");
        StringPrecondition.requireNonBlank(content, "content");
        ObjectPrecondition.requireNonNull(actorUserId, "actorUserId");
    }

    public static UpdateCommentCommand of(Long commentId, String content, UUID actorUserId) {
        return new UpdateCommentCommand(commentId, content, actorUserId);
    }
}
