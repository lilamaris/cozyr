package com.lilamaris.cozyr.board.application.port.in.query;

import com.lilamaris.cozyr.board.application.model.post.PostCursor;
import com.lilamaris.cozyr.board.application.model.post.PostFilter;
import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record ListPostSummaryQuery(
        UUID boardId,
        PostFilter filter,
        @Nullable PostCursor cursor,
        int size
) {
    public ListPostSummaryQuery {
        ObjectPrecondition.requireNonNull(boardId, "boardId");
        ObjectPrecondition.requireNonNull(filter, "filter");
        NumberPrecondition.requirePositive(size, "size");
    }

    public static ListPostSummaryQuery of(UUID boardId, PostFilter filter, PostCursor cursor, int size) {
        return new ListPostSummaryQuery(boardId, filter, cursor, size);
    }
}
