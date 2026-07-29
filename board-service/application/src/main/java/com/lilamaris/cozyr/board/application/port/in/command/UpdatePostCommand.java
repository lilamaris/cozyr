package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.UUID;

public record UpdatePostCommand(
        long postId,
        String title,
        String content,
        UUID actorUserId
) {
    public UpdatePostCommand {
        NumberPrecondition.requireNonNegative(postId, "postId");
        StringPrecondition.requireNonBlank(title, "title");
        StringPrecondition.requireNonBlank(content, "content");
        ObjectPrecondition.requireNonNull(actorUserId, "actorUserId");
    }

    public static UpdatePostCommand of(long postId, String title, String content, UUID actorUserId) {
        return new UpdatePostCommand(postId, title, content, actorUserId);
    }
}
