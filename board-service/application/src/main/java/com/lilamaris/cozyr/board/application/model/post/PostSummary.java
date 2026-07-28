package com.lilamaris.cozyr.board.application.model.post;

import com.lilamaris.cozyr.board.application.model.user.UserProjection;
import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.time.Instant;

public record PostSummary(
        long postId,
        String title,
        String content,
        Instant createdAt,
        UserProjection author
) {
    public PostSummary {
        NumberPrecondition.requireNonNegative(postId, "postId");
        StringPrecondition.requireNonBlank(title, "title");
        StringPrecondition.requireNonBlank(content, "content");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        ObjectPrecondition.requireNonNull(author, "author");
    }

    public static PostSummary of(long postId, String title, String content, Instant createdAt, UserProjection author) {
        return new PostSummary(postId, title, content, createdAt, author);
    }

    public PostSummary truncate(int maxPreviewLength) {
        var truncated = content.length() > maxPreviewLength
                ? content.substring(0, maxPreviewLength)
                : content;
        return PostSummary.of(postId, title, truncated, createdAt, author);
    }
}
