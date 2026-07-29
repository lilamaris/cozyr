package com.lilamaris.cozyr.board.application.port.in.result;

import com.lilamaris.cozyr.board.domain.Category;

import java.time.Instant;
import java.util.UUID;

public record UpdatedCategoryResult(
        UUID categoryId,
        UUID boardId,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
    public static UpdatedCategoryResult of(Category category) {
        return new UpdatedCategoryResult(
                category.getId(),
                category.getBoardId(),
                category.getName(),
                category.getDescription(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
