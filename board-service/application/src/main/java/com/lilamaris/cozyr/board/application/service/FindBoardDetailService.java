package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.model.board.BoardDetail;
import com.lilamaris.cozyr.board.application.port.in.FindBoardDetailUseCase;
import com.lilamaris.cozyr.board.application.port.in.query.FindBoardDetailQuery;
import com.lilamaris.cozyr.board.application.port.out.BoardReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindBoardDetailService implements FindBoardDetailUseCase {
    private final BoardReader reader;

    public FindBoardDetailService(BoardReader reader) {
        this.reader = reader;
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDetail find(FindBoardDetailQuery query) {
        var boardId = query.boardId();
        return reader.findDetailById(boardId)
                .orElseThrow(() -> new ApplicationException(BoardServiceProgressCode.BOARD_NOT_FOUND));
    }
}
