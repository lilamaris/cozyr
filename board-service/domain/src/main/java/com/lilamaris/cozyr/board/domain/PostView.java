package com.lilamaris.cozyr.board.domain;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "post_view")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostView {
    @Id
    private long postId;

    @Column(name = "count", nullable = false)
    private long count;

    @Column(name = "last_viewed_at", nullable = false)
    private Instant lastViewedAt;

    private PostView(long postId, long count, Instant lastViewedAt) {
        this.postId = NumberPrecondition.requireNonNegative(postId, "postId");
        this.count = NumberPrecondition.requireNonNegative(count, "count");
        this.lastViewedAt = ObjectPrecondition.requireNonNull(lastViewedAt, "lastViewedAt");
    }

    public static PostView of(long postId, long count, Instant lastViewedAt) {
        return new PostView(postId, count, lastViewedAt);
    }
}
