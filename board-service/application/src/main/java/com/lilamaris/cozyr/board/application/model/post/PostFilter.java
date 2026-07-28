package com.lilamaris.cozyr.board.application.model.post;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record PostFilter(
        @Nullable String title,
        @Nullable String content,
        @Nullable UUID authorUserId
) {
    public static PostFilter empty() {
        return new PostFilter(null, null, null);
    }

    public PostFilter withTitle(String title) {
        return new PostFilter(title, content, authorUserId);
    }

    public PostFilter withContent(String content) {
        return new PostFilter(title, content, authorUserId);
    }

    public PostFilter withAuthorUserId(UUID authorUserId) {
        return new PostFilter(title, content, authorUserId);
    }
}
