package com.lilamaris.cozyr.board.persistence.jpa.row;

import com.lilamaris.cozyr.board.application.model.comment.CommentDetail;
import com.lilamaris.cozyr.board.application.model.user.UserProjection;

import java.time.Instant;
import java.util.UUID;

public class CommentRow {
    public record Detail(
            long commentId,
            Long parentId,
            long postId,
            String content,
            Instant createdAt,
            Instant updatedAt,
            UUID authorUserId,
            String displayName
    ) {
        public CommentDetail toDetail() {
            var userProjection = UserProjection.of(authorUserId, displayName);
            return CommentDetail.of(commentId, parentId, postId, content, createdAt, updatedAt, userProjection);
        }
    }
}
