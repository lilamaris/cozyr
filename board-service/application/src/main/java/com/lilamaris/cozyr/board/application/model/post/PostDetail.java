package com.lilamaris.cozyr.board.application.model.post;

import com.lilamaris.cozyr.board.application.model.user.UserProjection;
import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

public record PostDetail(
        long postId,
        UUID boardId,
        String title,
        String content,
        Instant createdAt,
        @Nullable Instant updatedAt,
        UserProjection author
) {
    public PostDetail {
        NumberPrecondition.requireNonNegative(postId, "postId");
        ObjectPrecondition.requireNonNull(boardId, "boardId");
        StringPrecondition.requireNonBlank(title, "title");
        StringPrecondition.requireNonBlank(content, "content");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        ObjectPrecondition.requireNonNull(author, "author");
    }

    public static PostDetail of(long postId, UUID boardId, String title, String content, Instant createdAt, Instant updatedAt, UserProjection author) {
        return new PostDetail(postId, boardId, title, content, createdAt, updatedAt, author);
    }
}
