package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.ApplicationErrorCode;
import com.lilamaris.cozyr.board.application.model.board.BoardDetail;
import com.lilamaris.cozyr.board.application.port.in.FindBoardUseCase;
import com.lilamaris.cozyr.board.application.port.in.query.FindBoardQuery;
import com.lilamaris.cozyr.board.application.port.out.BoardReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import org.springframework.stereotype.Service;

@Service
public class FindBoardService implements FindBoardUseCase {
    private final BoardReader reader;

    public FindBoardService(BoardReader reader) {
        this.reader = reader;
    }

    @Override
    public BoardDetail find(FindBoardQuery query) {
        var boardId = query.boardId();
        return reader.findDetailById(boardId)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorCode.BOARD_NOT_FOUND));
    }
}
