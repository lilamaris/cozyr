package com.lilamaris.cozyr.board.application.port.in.result;

import com.lilamaris.cozyr.board.domain.Category;

import java.time.Instant;
import java.util.UUID;

public record CreatedCategoryResult(
        UUID categoryId,
        UUID boardId,
        String name,
        String description,
        Instant createdAt
) {
    public static CreatedCategoryResult of(Category category) {
        return new CreatedCategoryResult(
                category.getId(),
                category.getBoardId(),
                category.getName(),
                category.getDescription(),
                category.getCreatedAt()
        );
    }
}
