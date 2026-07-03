package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.model.board.BoardCursor;
import com.lilamaris.cozyr.board.application.model.board.BoardDetail;
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
    public CursorResult<BoardDetail, BoardCursor> list(ListBoardQuery query) {
        var filter = query.filter();
        var request = CursorRequest.of(query.cursor(), query.size());
        return reader.findDetailBy(filter, request);
    }
}
