package com.lilamaris.cozyr.board.domain;

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
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "board_id", nullable = false, updatable = false)
    private UUID boardId;

    @Column(name = "title")
    private String title;

    @Column(name = "content", length = 5000)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    private Post(
            UUID boardId,
            String title,
            String content,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.boardId = ObjectPrecondition.requireNonNull(boardId, "boardId");
        this.title = StringPrecondition.requireNonBlank(title, "title");
        this.content = StringPrecondition.requireNonBlank(content, "content");
        this.createdAt = ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        this.updatedAt = updatedAt;

        if (updatedAt != null) {
            TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        }
    }

    public static Post of(UUID boardId, String title, String content, Instant createdAt) {
        return new Post(boardId, title, content, createdAt, null);
    }

    public void updateTitle(String title, Instant updatedAt) {
        this.updatedAt = TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        this.title = StringPrecondition.requireNonBlank(title, "title");
    }

    public void updateContent(String content, Instant updatedAt) {
        this.updatedAt = TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        this.content = StringPrecondition.requireNonBlank(content, "content");
    }
}
