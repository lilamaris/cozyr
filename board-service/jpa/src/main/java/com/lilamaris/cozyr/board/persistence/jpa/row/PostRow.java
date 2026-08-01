package com.lilamaris.cozyr.board.persistence.jpa.row;

import com.lilamaris.cozyr.board.application.model.category.CategorySummary;
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
            UUID categoryId,
            String categoryName,
            UUID authorUserId,
            String displayName,
            long viewCount
    ) {
        public PostDetail toDetail() {
            var userProjection = UserProjection.of(authorUserId, displayName);
            var category = CategorySummary.of(categoryId, categoryName);
            return PostDetail.of(postId, boardId, title, content, createdAt, updatedAt, category, userProjection, viewCount);
        }
    }

    public record Summary(
            long postId,
            String title,
            String content,
            Instant createdAt,
            UUID categoryId,
            String categoryName,
            UUID authorUserId,
            String displayName,
            long viewCount
    ) {
        public PostSummary toSummary() {
            var userProjection = UserProjection.of(authorUserId, displayName);
            var category = CategorySummary.of(categoryId, categoryName);
            return PostSummary.of(postId, title, content, createdAt, category, userProjection, viewCount);
        }
    }
}
