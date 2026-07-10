package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

public record UpdateCommentCommand(
        Long commentId,
        String content
) {
    public UpdateCommentCommand {
        NumberPrecondition.requireNonNegative(commentId, "commentId");
        StringPrecondition.requireNonBlank(content, "content");
    }

    public static UpdateCommentCommand of(Long commentId, String content) {
        return new UpdateCommentCommand(commentId, content);
    }
}
