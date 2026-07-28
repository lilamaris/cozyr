package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.model.board.BoardCursor;
import com.lilamaris.cozyr.board.application.model.board.BoardSummary;
import com.lilamaris.cozyr.board.application.port.in.query.ListBoardSummaryQuery;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;

public interface ListBoardSummaryUseCase {
    CursorResult<BoardSummary, BoardCursor> list(ListBoardSummaryQuery query);
}
