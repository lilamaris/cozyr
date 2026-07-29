package com.lilamaris.cozyr.board.application.port.out;

import com.lilamaris.cozyr.board.domain.Category;

public interface CategoryStore {
    Category save(Category category);

    void delete(Category category);
}
