package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.UUID;

public record CreatePostCommand(
        UUID boardId,
        String title,
        String content,
        UUID authorUserId
) {
    public CreatePostCommand {
        ObjectPrecondition.requireNonNull(boardId, "boardId");
        StringPrecondition.requireNonBlank(title, "title");
        StringPrecondition.requireNonBlank(content, "content");
        ObjectPrecondition.requireNonNull(authorUserId, "authorUserId");
    }

    public static CreatePostCommand of(UUID boardId, String title, String content, UUID authorUserId) {
        return new CreatePostCommand(boardId, title, content, authorUserId);
    }
}
