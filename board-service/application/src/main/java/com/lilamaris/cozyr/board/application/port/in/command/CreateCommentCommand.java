package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.UUID;

public record CreateCommentCommand(
        Long postId,
        String content,
        UUID authorUserId
) {
    public CreateCommentCommand {
        NumberPrecondition.requireNonNegative(postId, "postId");
        StringPrecondition.requireNonBlank(content, "content");
        ObjectPrecondition.requireNonNull(authorUserId, "authorUserId");
    }

    public static CreateCommentCommand of(Long postId, String content, UUID authorUserId) {
        return new CreateCommentCommand(postId, content, authorUserId);
    }
}
