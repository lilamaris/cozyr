package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.model.board.BoardCursor;
import com.lilamaris.cozyr.board.application.model.board.BoardSummary;
import com.lilamaris.cozyr.board.application.model.cursor.CursorRequest;
import com.lilamaris.cozyr.board.application.model.cursor.CursorResult;
import com.lilamaris.cozyr.board.application.port.in.ListBoardSummaryUseCase;
import com.lilamaris.cozyr.board.application.port.in.query.ListBoardSummaryQuery;
import com.lilamaris.cozyr.board.application.port.out.BoardReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListBoardSummaryService implements ListBoardSummaryUseCase {
    private final BoardReader reader;

    public ListBoardSummaryService(BoardReader reader) {
        this.reader = reader;
    }

    @Override
    @Transactional(readOnly = true)
    public CursorResult<BoardSummary, BoardCursor> list(ListBoardSummaryQuery query) {
        var filter = query.filter();
        var request = CursorRequest.of(query.cursor(), query.size());
        return reader.findSummaries(filter, request)
                .map(summary -> summary.truncate(30));
    }
}
