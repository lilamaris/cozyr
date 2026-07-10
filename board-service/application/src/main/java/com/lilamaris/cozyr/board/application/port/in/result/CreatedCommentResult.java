package com.lilamaris.cozyr.board.application.port.in.result;

import com.lilamaris.cozyr.board.domain.Comment;

import java.time.Instant;

public record CreatedCommentResult(
        Long commentId,
        Long postId,
        String content,
        Instant createdAt
) {
    public static CreatedCommentResult of(Comment comment) {
        return new CreatedCommentResult(
                comment.getId(),
                comment.getPostId(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
