package com.lilamaris.cozyr.statistics.domain;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyNewCommentId {
    @Column(name = "post_id", nullable = false)
    private long postId;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    private DailyNewCommentId(long postId, LocalDate createdDate) {
        this.postId = NumberPrecondition.requireNonNegative(postId, "postId");
        this.createdDate = ObjectPrecondition.requireNonNull(createdDate, "createdDate");
    }

    public static DailyNewCommentId of(long postId, LocalDate createdDate) {
        return new DailyNewCommentId(postId, createdDate);
    }
}
