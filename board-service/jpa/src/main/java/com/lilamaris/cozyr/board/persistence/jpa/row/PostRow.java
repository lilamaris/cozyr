package com.lilamaris.cozyr.board.persistence.jpa.row;

import com.lilamaris.cozyr.board.application.model.post.PostDetail;
import com.lilamaris.cozyr.board.application.model.post.PostSummary;
import com.lilamaris.cozyr.board.application.model.user.UserProjection;

import java.time.Instant;
import java.util.UUID;

public class PostRow {
    public record Detail(
            long postId,
            UUID boardId,
            String title,
            String content,
            Instant createdAt,
            Instant updatedAt,
            UUID authorUserId,
            String displayName
    ) {
        public PostDetail toDetail() {
            var userProjection = UserProjection.of(authorUserId, displayName);
            return PostDetail.of(postId, boardId, title, content, createdAt, updatedAt, userProjection);
        }
    }

    public record Summary(
            long postId,
            String title,
            String content,
            Instant createdAt,
            UUID authorUserId,
            String displayName
    ) {
        public PostSummary toSummary() {
            var userProjection = UserProjection.of(authorUserId, displayName);
            return PostSummary.of(postId, title, content, createdAt, userProjection);
        }
    }
}
