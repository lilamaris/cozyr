package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.port.in.CreateCategoryUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.CreateCategoryCommand;
import com.lilamaris.cozyr.board.application.port.in.result.CreatedCategoryResult;
import com.lilamaris.cozyr.board.application.port.out.BoardReader;
import com.lilamaris.cozyr.board.application.port.out.CategoryStore;
import com.lilamaris.cozyr.board.domain.Category;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class CreateCategoryService implements CreateCategoryUseCase {
    private final BoardReader boardReader;
    private final CategoryStore store;
    private final Clock clock;

    @Override
    @Transactional
    public CreatedCategoryResult create(CreateCategoryCommand command) {
        var boardId = command.boardId();
        if (!boardReader.existsById(boardId))
            throw new ApplicationException(BoardServiceProgressCode.BOARD_NOT_FOUND);

        var now = clock.instant();
        var name = command.name();
        var description = command.description();

        var category = Category.of(boardId, name, description, now);
        var saved = store.save(category);

        return CreatedCategoryResult.of(saved);
    }
}
