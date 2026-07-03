package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.model.board.BoardCursor;
import com.lilamaris.cozyr.board.application.model.board.BoardSummary;
import com.lilamaris.cozyr.board.application.model.cursor.CursorRequest;
import com.lilamaris.cozyr.board.application.model.cursor.CursorResult;
import com.lilamaris.cozyr.board.application.port.in.ListBoardUseCase;
import com.lilamaris.cozyr.board.application.port.in.query.ListBoardQuery;
import com.lilamaris.cozyr.board.application.port.out.BoardReader;
import org.springframework.stereotype.Service;

@Service
public class ListBoardService implements ListBoardUseCase {
    private final BoardReader reader;

    public ListBoardService(BoardReader reader) {
        this.reader = reader;
    }

    @Override
    public CursorResult<BoardSummary, BoardCursor> list(ListBoardQuery query) {
        var filter = query.filter();
        var request = CursorRequest.of(query.cursor(), query.size());
        return reader.findSummaries(filter, request)
                .map(summary -> summary.truncate(30));
    }
}
