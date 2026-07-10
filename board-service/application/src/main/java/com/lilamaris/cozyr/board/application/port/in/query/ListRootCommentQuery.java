package com.lilamaris.cozyr.board.application.port.in.query;

import com.lilamaris.cozyr.board.application.model.comment.CommentCursor;
import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import org.jspecify.annotations.Nullable;

public record ListRootCommentQuery(
        Long postId,
        @Nullable CommentCursor cursor,
        int size
) {
    public ListRootCommentQuery {
        NumberPrecondition.requireNonNegative(postId, "postId");
        NumberPrecondition.requirePositive(size, "size");
    }

    public static ListRootCommentQuery of(Long postId, @Nullable CommentCursor cursor, int size) {
        return new ListRootCommentQuery(postId, cursor, size);
    }
}
