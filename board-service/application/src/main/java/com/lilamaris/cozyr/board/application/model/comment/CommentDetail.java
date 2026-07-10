package com.lilamaris.cozyr.board.application.model.comment;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

public record CommentDetail(
        Long commentId,
        @Nullable Long parentId,
        Long postId,
        String content,
        Instant createdAt,
        @Nullable Instant updatedAt
) {
    public CommentDetail {
        NumberPrecondition.requireNonNegative(commentId, "commentId");
        NumberPrecondition.requireNonNegative(postId, "postId");
        StringPrecondition.requireNonBlank(content, "content");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
    }

    public static CommentDetail of(Long commentId, Long parentId, Long postId, String content, Instant createdAt, Instant updatedAt) {
        return new CommentDetail(commentId, parentId, postId, content, createdAt, updatedAt);
    }
}
