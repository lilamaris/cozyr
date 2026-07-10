package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;

public record DeleteCommentCommand(
        Long commentId
) {
    public DeleteCommentCommand {
        NumberPrecondition.requireNonNegative(commentId, "commentId");
    }

    public static DeleteCommentCommand of(Long commentId) {
        return new DeleteCommentCommand(commentId);
    }
}
