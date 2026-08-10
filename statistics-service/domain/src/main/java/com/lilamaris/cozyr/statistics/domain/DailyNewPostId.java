package com.lilamaris.cozyr.statistics.domain;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyNewPostId implements Serializable {
    @Column(name = "board_id", nullable = false)
    private UUID boardId;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    private DailyNewPostId(UUID boardId, LocalDate createdDate) {
        this.boardId = ObjectPrecondition.requireNonNull(boardId, "boardId");
        this.createdDate = ObjectPrecondition.requireNonNull(createdDate, "createdDate");
    }

    public static DailyNewPostId of(UUID boardId, LocalDate createdDate) {
        return new DailyNewPostId(boardId, createdDate);
    }
}
