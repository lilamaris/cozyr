package com.lilamaris.cozyr.board.application.port.in.query;

import com.lilamaris.cozyr.board.application.model.board.BoardCursor;
import com.lilamaris.cozyr.board.application.model.board.BoardFilter;
import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import org.jspecify.annotations.Nullable;

public record ListBoardSummaryQuery(
        BoardFilter filter,
        @Nullable BoardCursor cursor,
        int size
) {
    public ListBoardSummaryQuery {
        ObjectPrecondition.requireNonNull(filter, "filter");
        NumberPrecondition.requirePositive(size, "size");
    }

    public static ListBoardSummaryQuery of(BoardFilter filter, BoardCursor cursor, int size) {
        return new ListBoardSummaryQuery(filter, cursor, size);
    }
}
