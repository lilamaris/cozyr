package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

public record ReplyCommentCommand(
        Long parentId,
        String content
) {
    public ReplyCommentCommand {
        NumberPrecondition.requireNonNegative(parentId, "parentId");
        StringPrecondition.requireNonBlank(content, "content");
    }

    public static ReplyCommentCommand of(Long parentId, String content) {
        return new ReplyCommentCommand(parentId, content);
    }
}
