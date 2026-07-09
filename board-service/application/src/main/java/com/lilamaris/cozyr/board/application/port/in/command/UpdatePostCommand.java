package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

public record UpdatePostCommand(
        long postId,
        String title,
        String content
) {
    public UpdatePostCommand {
        NumberPrecondition.requireNonNegative(postId, "postId");
        StringPrecondition.requireNonBlank(title, "title");
        StringPrecondition.requireNonBlank(content, "content");
    }

    public static UpdatePostCommand of(long postId, String title, String content) {
        return new UpdatePostCommand(postId, title, content);
    }
}
