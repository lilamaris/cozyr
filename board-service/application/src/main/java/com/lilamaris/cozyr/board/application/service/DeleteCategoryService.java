package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.port.in.DeleteCategoryUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.DeleteCategoryCommand;
import com.lilamaris.cozyr.board.application.port.out.CategoryReader;
import com.lilamaris.cozyr.board.application.port.out.CategoryStore;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteCategoryService implements DeleteCategoryUseCase {
    private final CategoryReader reader;
    private final CategoryStore store;

    @Override
    @Transactional
    public void delete(DeleteCategoryCommand command) {
        var categoryId = command.categoryId();
        var category = reader.findById(categoryId)
                .orElseThrow(() -> new ApplicationException(BoardServiceProgressCode.CATEGORY_NOT_FOUND));

        store.delete(category);
    }
}
