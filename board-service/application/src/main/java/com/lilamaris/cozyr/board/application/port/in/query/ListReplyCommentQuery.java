package com.lilamaris.cozyr.board.application.port.in.query;

import com.lilamaris.cozyr.board.application.model.comment.CommentCursor;
import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import org.jspecify.annotations.Nullable;

public record ListReplyCommentQuery(
        Long parentId,
        @Nullable CommentCursor cursor,
        int size
) {
    public ListReplyCommentQuery {
        NumberPrecondition.requireNonNegative(parentId, "parentId");
        NumberPrecondition.requirePositive(size, "size");
    }

    public static ListReplyCommentQuery of(Long parentId, @Nullable CommentCursor cursor, int size) {
        return new ListReplyCommentQuery(parentId, cursor, size);
    }
}
