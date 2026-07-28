package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.UUID;

public record ReplyCommentCommand(
        Long parentId,
        String content,
        UUID authorUserId
) {
    public ReplyCommentCommand {
        NumberPrecondition.requireNonNegative(parentId, "parentId");
        StringPrecondition.requireNonBlank(content, "content");
        ObjectPrecondition.requireNonNull(authorUserId, "authorUserId");
    }

    public static ReplyCommentCommand of(Long parentId, String content, UUID authorUserId) {
        return new ReplyCommentCommand(parentId, content, authorUserId);
    }
}
