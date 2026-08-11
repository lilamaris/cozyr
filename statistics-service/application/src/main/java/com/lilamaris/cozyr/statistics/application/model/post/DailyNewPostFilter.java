package com.lilamaris.cozyr.statistics.application.model.post;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.TimePrecondition;

import java.time.LocalDate;

public record DailyNewPostFilter(
        LocalDate from,
        LocalDate to
) {
    public DailyNewPostFilter {
        ObjectPrecondition.requireNonNull(from, "from");
        TimePrecondition.requireAfter(to, from, "to", "from");
    }

    public static DailyNewPostFilter of(LocalDate from, LocalDate to) {
        return new DailyNewPostFilter(from, to);
    }
}