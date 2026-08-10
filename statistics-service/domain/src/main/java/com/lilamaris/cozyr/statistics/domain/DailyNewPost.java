package com.lilamaris.cozyr.statistics.domain;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.TimePrecondition;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "daily_new_post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyNewPost {
    @EmbeddedId
    private DailyNewPostId id;

    @Column(name = "created_count", nullable = false)
    private long createdCount;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    private DailyNewPost(DailyNewPostId id, long createdCount, Instant createdAt, Instant updatedAt) {
        this.id = ObjectPrecondition.requireNonNull(id, "id");
        this.createdCount = NumberPrecondition.requireNonNegative(createdCount, "createdCount");
        this.createdAt = ObjectPrecondition.requireNonNull(createdAt, "createdAt");

        if (updatedAt != null) {
            this.updatedAt = TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        }
    }

    public static DailyNewPost of(UUID boardId, LocalDate createdDate, long createdCount, Instant createdAt) {
        var id = DailyNewPostId.of(boardId, createdDate);
        return new DailyNewPost(id, createdCount, createdAt, createdAt);
    }
}
