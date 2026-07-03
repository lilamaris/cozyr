package com.lilamaris.cozyr.board.application.model.board;

import org.jspecify.annotations.Nullable;

public record BoardFilter(
        @Nullable String name,
        @Nullable String description
) {
    public static BoardFilter empty() {
        return new BoardFilter(null, null);
    }

    public BoardFilter withName(String name) {
        return new BoardFilter(name, description);
    }

    public BoardFilter withDescription(String description) {
        return new BoardFilter(name, description);
    }
}
