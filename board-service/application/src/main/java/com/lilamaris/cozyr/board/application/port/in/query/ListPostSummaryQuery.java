package com.lilamaris.cozyr.board.application.port.in.query;

import com.lilamaris.cozyr.board.application.model.post.PostCursor;
import com.lilamaris.cozyr.board.application.model.post.PostFilter;
import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import org.jspecify.annotations.Nullable;

public record ListPostSummaryQuery(
        PostFilter filter,
        @Nullable PostCursor cursor,
        int size
) {
    public ListPostSummaryQuery {
        ObjectPrecondition.requireNonNull(filter, "filter");
        NumberPrecondition.requirePositive(size, "size");
    }

    public static ListPostSummaryQuery of(PostFilter filter, PostCursor cursor, int size) {
        return new ListPostSummaryQuery(filter, cursor, size);
    }
}
