package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;

public record DeletePostCommand(
        Long postId
) {
    public DeletePostCommand {
        NumberPrecondition.requireNonNegative(postId, "postId");
    }

    public static DeletePostCommand of(Long postId) {
        return new DeletePostCommand(postId);
    }
}
