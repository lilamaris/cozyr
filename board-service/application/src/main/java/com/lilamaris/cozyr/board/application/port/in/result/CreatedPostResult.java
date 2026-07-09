package com.lilamaris.cozyr.board.application.port.in.result;

import com.lilamaris.cozyr.board.domain.Post;

import java.time.Instant;
import java.util.UUID;

public record CreatedPostResult(
        long postId,
        UUID boardId,
        String title,
        String content,
        Instant createdAt
) {
    public static CreatedPostResult from(Post post) {
        return new CreatedPostResult(
                post.getId(),
                post.getBoardId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt()
        );
    }
}
