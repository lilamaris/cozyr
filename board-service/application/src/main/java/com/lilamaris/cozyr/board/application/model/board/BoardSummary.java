package com.lilamaris.cozyr.board.application.model.board;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.time.Instant;
import java.util.UUID;

public record BoardSummary(
        UUID boardId,
        String name,
        String description,
        Instant createdAt
) {
    public BoardSummary {
        ObjectPrecondition.requireNonNull(boardId, "boardId");
        StringPrecondition.requireNonBlank(name, "name");
        StringPrecondition.requireNonBlank(description, "description");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
    }

    public static BoardSummary of(UUID boardId, String title, String description, Instant createdAt) {
        return new BoardSummary(boardId, title, description, createdAt);
    }

    public BoardSummary truncate(int maxPreviewLength) {
        var truncated = description.length() > maxPreviewLength
                ? description.substring(0, maxPreviewLength) + "..."
                : description;
        return BoardSummary.of(boardId, name, truncated, createdAt);
    }
}
