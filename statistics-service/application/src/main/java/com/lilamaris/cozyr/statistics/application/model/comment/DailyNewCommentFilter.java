package com.lilamaris.cozyr.statistics.application.model.comment;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.TimePrecondition;

import java.time.LocalDate;

public record DailyNewCommentFilter(
        LocalDate from,
        LocalDate to
) {
    public DailyNewCommentFilter {
        ObjectPrecondition.requireNonNull(from, "from");
        TimePrecondition.requireAfter(to, from, "to", "from");
    }

    public static DailyNewCommentFilter of(LocalDate from, LocalDate to) {
        return new DailyNewCommentFilter(from, to);
    }
}
