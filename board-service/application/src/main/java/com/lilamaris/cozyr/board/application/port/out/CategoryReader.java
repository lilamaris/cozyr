package com.lilamaris.cozyr.board.application.port.out;

import com.lilamaris.cozyr.board.domain.Category;

import java.util.Optional;
import java.util.UUID;

public interface CategoryReader {
    boolean existsById(UUID categoryId);

    Optional<Category> findById(UUID categoryId);
}
