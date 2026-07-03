package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.port.in.CreateBoardUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.CreateBoardCommand;
import com.lilamaris.cozyr.board.application.port.in.result.CreatedBoardResult;
import com.lilamaris.cozyr.board.application.port.out.BoardStore;
import com.lilamaris.cozyr.board.domain.Board;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class CreateBoardService implements CreateBoardUseCase {
    private final BoardStore store;
    private final Clock clock;

    public CreateBoardService(BoardStore store, Clock clock) {
        this.store = store;
        this.clock = clock;
    }

    @Override
    public CreatedBoardResult create(CreateBoardCommand command) {
        var now = clock.instant();
        var name = command.name();
        var description = command.description();

        var board = Board.of(name, description, now);
        var saved = store.save(board);

        return CreatedBoardResult.from(saved);
    }
}
