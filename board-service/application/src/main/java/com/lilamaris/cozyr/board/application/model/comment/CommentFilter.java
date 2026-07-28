package com.lilamaris.cozyr.board.application.model.comment;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record CommentFilter(
        @Nullable Long postId,
        @Nullable Long parentId,
        @Nullable UUID authorUserId
) {
    public static CommentFilter empty() {
        return new CommentFilter(null, null, null);
    }

    public CommentFilter withPostId(Long postId) {
        return new CommentFilter(postId, parentId, authorUserId);
    }

    public CommentFilter withParentId(Long parentId) {
        return new CommentFilter(postId, parentId, authorUserId);
    }

    public CommentFilter withAuthorUserId(UUID authorUserId) {
        return new CommentFilter(postId, parentId, authorUserId);
    }
}
