package com.lilamaris.cozyr.statistics.application.model.post;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.statistics.application.model.point.DailyPoint;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record DailyNewPostStatistics(
        UUID boardId,
        LocalDate from,
        LocalDate to,
        long totalCount,
        List<DailyPoint> points
) {
    public DailyNewPostStatistics {
        ObjectPrecondition.requireNonNull(boardId, "boardId");
        ObjectPrecondition.requireNonNull(from, "from");
        ObjectPrecondition.requireNonNull(to, "to");
        NumberPrecondition.requireNonNegative(totalCount, "totalCount");
        ObjectPrecondition.requireNonNull(points, "points");
    }

    public static DailyNewPostStatistics of(UUID boardId, LocalDate from, LocalDate to, long totalCount, List<DailyPoint> points) {
        return new DailyNewPostStatistics(boardId, from, to, totalCount, points);
    }
}
