package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.model.board.BoardCursor;
import com.lilamaris.cozyr.board.application.model.board.BoardSummary;
import com.lilamaris.cozyr.board.application.model.cursor.CursorResult;
import com.lilamaris.cozyr.board.application.port.in.query.ListBoardSummaryQuery;

public interface ListBoardSummaryUseCase {
    CursorResult<BoardSummary, BoardCursor> list(ListBoardSummaryQuery query);
}
