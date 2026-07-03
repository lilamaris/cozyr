package com.lilamaris.cozyr.board.application.port.out;

import com.lilamaris.cozyr.board.application.model.board.BoardCursor;
import com.lilamaris.cozyr.board.application.model.board.BoardDetail;
import com.lilamaris.cozyr.board.application.model.board.BoardFilter;
import com.lilamaris.cozyr.board.application.model.board.BoardSummary;
import com.lilamaris.cozyr.board.application.model.cursor.CursorRequest;
import com.lilamaris.cozyr.board.application.model.cursor.CursorResult;
import com.lilamaris.cozyr.board.domain.Board;

import java.util.Optional;
import java.util.UUID;

public interface BoardReader {
    Optional<Board> findById(UUID id);

    Optional<BoardDetail> findDetailById(UUID id);

    CursorResult<BoardSummary, BoardCursor> findSummaries(BoardFilter filter, CursorRequest<BoardCursor> request);
}
