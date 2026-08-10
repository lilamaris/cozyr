package com.lilamaris.cozyr.statistics.application.model.post;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.time.LocalDate;

public record DailyNewPostPoint(
        LocalDate date,
        long count
) {
    public DailyNewPostPoint {
        ObjectPrecondition.requireNonNull(date, "date");
        NumberPrecondition.requireNonNegative(count, "count");
    }

    public static DailyNewPostPoint of(LocalDate date, long count) {
        return new DailyNewPostPoint(date, count);
    }
}
