package com.lilamaris.cozyr.board.application.model.cursor;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import org.jspecify.annotations.Nullable;

import java.util.List;

public record CursorResult<T, C>(
        List<T> content,
        @Nullable C nextCursor,
        boolean hasNext
) {
    public CursorResult {
        ObjectPrecondition.requireNonNull(content, "content");
    }

    public static <T, C> CursorResult<T, C> of(List<T> content, C nextCursor, boolean hasNext) {
        return new CursorResult<>(content, nextCursor, hasNext);
    }
}
