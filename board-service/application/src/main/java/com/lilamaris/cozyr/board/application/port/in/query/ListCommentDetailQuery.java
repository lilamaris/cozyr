package com.lilamaris.cozyr.board.application.port.in.query;

import com.lilamaris.cozyr.board.application.model.comment.CommentCursor;
import com.lilamaris.cozyr.board.application.model.comment.CommentFilter;
import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import org.jspecify.annotations.Nullable;

public record ListCommentDetailQuery(
        CommentFilter filter,
        @Nullable CommentCursor cursor,
        int size
) {
    public ListCommentDetailQuery {
        ObjectPrecondition.requireNonNull(filter, "filter");
        NumberPrecondition.requirePositive(size, "size");
    }

    public static ListCommentDetailQuery of(CommentFilter filter, @Nullable CommentCursor cursor, int size) {
        return new ListCommentDetailQuery(filter, cursor, size);
    }
}
