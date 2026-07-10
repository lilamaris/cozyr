package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

public record CreateCommentCommand(
        Long postId,
        String content
) {
    public CreateCommentCommand {
        NumberPrecondition.requireNonNegative(postId, "postId");
        StringPrecondition.requireNonBlank(content, "content");
    }

    public static CreateCommentCommand of(Long postId, String content) {
        return new CreateCommentCommand(postId, content);
    }
}
