package com.lilamaris.cozyr.board.application.model.board;

import com.lilamaris.cozyr.board.application.model.category.CategorySummary;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record BoardDetail(
        UUID boardId,
        String name,
        String description,
        Instant createdAt,
        @Nullable Instant updatedAt,
        List<CategorySummary> categories
) {
    public BoardDetail {
        ObjectPrecondition.requireNonNull(boardId, "boardId");
        StringPrecondition.requireNonBlank(name, "name");
        StringPrecondition.requireNonBlank(description, "description");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        ObjectPrecondition.requireNonNull(categories, "categories");
    }

    public static BoardDetail of(UUID boardId, String name, String description, Instant createdAt, Instant updatedAt, List<CategorySummary> categories) {
        return new BoardDetail(boardId, name, description, createdAt, updatedAt, categories);
    }
}
