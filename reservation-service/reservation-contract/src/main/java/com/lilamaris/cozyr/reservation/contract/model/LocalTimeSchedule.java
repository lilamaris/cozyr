package com.lilamaris.cozyr.reservation.contract.model;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.TimePrecondition;

import java.time.Duration;
import java.time.LocalTime;

public record LocalTimeSchedule(
        LocalTime from,
        LocalTime to,
        boolean isOverMidnight
) {
    public LocalTimeSchedule {
        ObjectPrecondition.requireNonNull(from, "from");

        if (isOverMidnight) {
            TimePrecondition.requireBefore(to, from, "to", "from");
        } else {
            TimePrecondition.requireAfter(to, from, "to", "from");
        }
    }

    public static LocalTimeSchedule of(LocalTime from, LocalTime to) {
        return new LocalTimeSchedule(from, to, from.isAfter(to));
    }

    public Duration range() {
        var range = Duration.between(from, to);
        if (isOverMidnight) range = range.plusDays(1);
        return range;
    }
}