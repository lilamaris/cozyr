package com.lilamaris.cozyr.statistics.application.model.point;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.time.LocalDate;

public record DailyPoint(
        LocalDate date,
        long count
) {
    public DailyPoint {
        ObjectPrecondition.requireNonNull(date, "date");
    }

    public static DailyPoint of(LocalDate date, long count) {
        return new DailyPoint(date, count);
    }
}
