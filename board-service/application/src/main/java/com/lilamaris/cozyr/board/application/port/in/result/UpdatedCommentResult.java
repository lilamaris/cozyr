package com.lilamaris.cozyr.board.application.port.in.result;

import com.lilamaris.cozyr.board.domain.Comment;

import java.time.Instant;

public record UpdatedCommentResult(
        Long commentId,
        Long postId,
        Long parentId,
        String content,
        Instant createdAt,
        Instant updatedAt
) {
    public static UpdatedCommentResult of(Comment comment) {
        return new UpdatedCommentResult(
                comment.getId(),
                comment.getPostId(),
                comment.getParentId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
