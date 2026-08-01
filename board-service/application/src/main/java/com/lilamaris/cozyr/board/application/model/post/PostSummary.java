package com.lilamaris.cozyr.board.application.model.post;

import com.lilamaris.cozyr.board.application.model.category.CategorySummary;
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
        CategorySummary category,
        UserProjection author,
        long viewCount
) {
    public PostSummary {
        NumberPrecondition.requireNonNegative(postId, "postId");
        StringPrecondition.requireNonBlank(title, "title");
        StringPrecondition.requireNonBlank(content, "content");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        ObjectPrecondition.requireNonNull(category, "category");
        ObjectPrecondition.requireNonNull(author, "author");
        NumberPrecondition.requireNonNegative(viewCount, "viewCount");
    }

    public static PostSummary of(long postId, String title, String content, Instant createdAt, CategorySummary category, UserProjection author, long viewCount) {
        return new PostSummary(postId, title, content, createdAt, category, author, viewCount);
    }

    public PostSummary truncate(int maxPreviewLength) {
        var truncated = content.length() > maxPreviewLength
                ? content.substring(0, maxPreviewLength)
                : content;
        return PostSummary.of(postId, title, truncated, createdAt, category, author, viewCount);
    }
}
