package com.lilamaris.cozyr.board.application.model.post;

import org.jspecify.annotations.Nullable;

public record PostFilter(
        @Nullable String title,
        @Nullable String content
) {
    public static PostFilter empty() {
        return new PostFilter(null, null);
    }

    public PostFilter withTitle(String title) {
        return new PostFilter(title, content);
    }

    public PostFilter withContent(String content) {
        return new PostFilter(title, content);
    }
}
