package com.lilamaris.cozyr.statistics.application.model.comment;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.TimePrecondition;
import com.lilamaris.cozyr.statistics.application.model.point.DailyPoint;

import java.time.LocalDate;
import java.util.List;

public record DailyNewCommentStatistics(
        LocalDate from,
        LocalDate to,
        long totalCount,
        List<DailyPoint> points
) {
    public DailyNewCommentStatistics {
        ObjectPrecondition.requireNonNull(from, "from");
        TimePrecondition.requireAfter(to, from, "to", "from");
        ObjectPrecondition.requireNonNull(points, "points");
    }

    public static DailyNewCommentStatistics of(LocalDate from, LocalDate to, long totalCount, List<DailyPoint> points) {
        return new DailyNewCommentStatistics(from, to, totalCount, points);
    }
}
