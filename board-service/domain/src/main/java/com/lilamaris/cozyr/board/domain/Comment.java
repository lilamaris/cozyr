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

    private Comment(Long postId, Long parentId, String content, Instant createdAt, Instant updatedAt) {
        this.postId = NumberPrecondition.requireNonNegative(postId, "postId");
        this.content = StringPrecondition.requireNonBlank(content, "content");
        this.createdAt = ObjectPrecondition.requireNonNull(createdAt, "createdAt");

        if (parentId != null) {
            this.parentId = NumberPrecondition.requireNonNegative(parentId, "parentId");
        }

        if (updatedAt != null) {
            this.updatedAt = TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        }
    }

    public static Comment root(Long postId, String content, Instant createdAt) {
        return new Comment(postId, null, content, createdAt, createdAt);
    }

    public static Comment reply(Long postId, Long parentId, String content, Instant createdAt) {
        return new Comment(postId, parentId, content, createdAt, createdAt);
    }

    public static Comment reply(Comment parent, String content, Instant createdAt) {
        return new Comment(parent.getPostId(), parent.getId(), content, createdAt, createdAt);
    }

    public void updateContent(String content, Instant updatedAt) {
        this.updatedAt = TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        this.content = content;
    }
}
