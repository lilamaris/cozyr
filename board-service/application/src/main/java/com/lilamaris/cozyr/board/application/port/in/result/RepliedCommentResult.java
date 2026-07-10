package com.lilamaris.cozyr.board.application.port.in.result;

import com.lilamaris.cozyr.board.domain.Comment;

import java.time.Instant;

public record RepliedCommentResult(
        Long commentId,
        Long postId,
        Long parentId,
        String content,
        Instant createdAt
) {
    public static RepliedCommentResult of(Comment comment) {
        return new RepliedCommentResult(
                comment.getId(),
                comment.getPostId(),
                comment.getParentId(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
