package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.model.board.BoardCursor;
import com.lilamaris.cozyr.board.application.model.board.BoardDetail;
import com.lilamaris.cozyr.board.application.model.cursor.CursorResult;
import com.lilamaris.cozyr.board.application.port.in.query.ListBoardQuery;

public interface ListBoardUseCase {
    CursorResult<BoardDetail, BoardCursor> list(ListBoardQuery query);
}
