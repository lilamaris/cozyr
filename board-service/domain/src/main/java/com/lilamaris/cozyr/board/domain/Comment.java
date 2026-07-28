package com.lilamaris.cozyr.board.domain;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.TimePrecondition;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "post_id", nullable = false, updatable = false)
    private Long postId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "author_user_id", nullable = false)
    private UUID authorUserId;

    private Comment(Long postId, Long parentId, String content, Instant createdAt, Instant updatedAt, boolean deleted, Instant deletedAt, UUID authorUserId) {
        this.postId = NumberPrecondition.requireNonNegative(postId, "postId");
        this.content = StringPrecondition.requireNonBlank(content, "content");
        this.createdAt = ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        this.deleted = deleted;
        this.authorUserId = ObjectPrecondition.requireNonNull(authorUserId, "authorUserId");

        if (parentId != null) {
            this.parentId = NumberPrecondition.requireNonNegative(parentId, "parentId");
        }

        if (updatedAt != null) {
            this.updatedAt = TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        }

        if (deleted) {
            this.deletedAt = TimePrecondition.requireAfterOrEqual(deletedAt, createdAt, "deletedAt", "createdAt");
        }
    }

    public static Comment root(Long postId, String content, Instant createdAt, UUID authorUserId) {
        return new Comment(postId, null, content, createdAt, createdAt, false, null, authorUserId);
    }

    public static Comment reply(Long postId, Long parentId, String content, Instant createdAt, UUID authorUserId) {
        return new Comment(postId, parentId, content, createdAt, createdAt, false, null, authorUserId);
    }

    public static Comment reply(Comment parent, String content, Instant createdAt, UUID authorUserId) {
        return new Comment(parent.getPostId(), parent.getId(), content, createdAt, createdAt, false, null, authorUserId);
    }

    public void updateContent(String content, Instant updatedAt) {
        this.updatedAt = TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        this.content = content;
    }

    public void delete(Instant deletedAt) {
        if (deleted) return;
        this.deletedAt = TimePrecondition.requireAfterOrEqual(deletedAt, createdAt, "deletedAt", "createdAt");
        this.deleted = true;
    }
}
