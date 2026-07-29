package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.port.in.UpdateCategoryUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.UpdateCategoryCommand;
import com.lilamaris.cozyr.board.application.port.in.result.UpdatedCategoryResult;
import com.lilamaris.cozyr.board.application.port.out.CategoryReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class UpdateCategoryService implements UpdateCategoryUseCase {
    private final CategoryReader reader;
    private final Clock clock;

    @Override
    @Transactional
    public UpdatedCategoryResult update(UpdateCategoryCommand command) {
        var categoryId = command.categoryId();
        var category = reader.findById(categoryId)
                .orElseThrow(() -> new ApplicationException(BoardServiceProgressCode.CATEGORY_NOT_FOUND));

        var now = clock.instant();
        var name = command.name();
        var description = command.description();

        category.updateName(name, now);
        category.updateDescription(description, now);

        return UpdatedCategoryResult.of(category);
    }
}
