package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.port.in.UpdateBoardUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.UpdateBoardCommand;
import com.lilamaris.cozyr.board.application.port.in.result.UpdatedBoardResult;
import com.lilamaris.cozyr.board.application.port.out.BoardReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class UpdateBoardService implements UpdateBoardUseCase {
    private final BoardReader reader;
    private final Clock clock;

    public UpdateBoardService(BoardReader reader, Clock clock) {
        this.reader = reader;
        this.clock = clock;
    }

    @Override
    @Transactional
    public UpdatedBoardResult update(UpdateBoardCommand command) {
        var boardId = command.boardId();
        var board = reader.findById(boardId)
                .orElseThrow(() -> new ApplicationException(BoardServiceProgressCode.BOARD_NOT_FOUND));

        var now = clock.instant();
        var name = command.name();
        var description = command.description();
        board.updateName(name, now);
        board.updateDescription(description, now);

        return UpdatedBoardResult.from(board);
    }
}
