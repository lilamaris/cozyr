package com.lilamaris.cozyr.board.application.model.category;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.UUID;

public record CategorySummary(
        UUID categoryId,
        String name
) {
    public CategorySummary {
        ObjectPrecondition.requireNonNull(categoryId, "categoryId");
        StringPrecondition.requireNonBlank(name, "name");
    }

    public static CategorySummary of(UUID categoryId, String name) {
        return new CategorySummary(categoryId, name);
    }
}
