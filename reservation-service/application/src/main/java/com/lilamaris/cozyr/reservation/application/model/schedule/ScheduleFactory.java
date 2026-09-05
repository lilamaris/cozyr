package com.lilamaris.cozyr.reservation.application.model.schedule;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.TimePrecondition;
import com.lilamaris.cozyr.reservation.contract.model.LocalTimeSchedule;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ScheduleFactory {
    public List<LocalTimeSchedule> create(LocalTime startTime, LocalTime endTime, Duration step) {
        ObjectPrecondition.requireNonNull(startTime, "startTime");
        ObjectPrecondition.requireNonNull(endTime, "endTime");
        TimePrecondition.requirePositive(step, "step");

        var remaining = Duration.between(startTime, endTime);
        if (!remaining.isPositive()) remaining = remaining.plusDays(1);
        if (!remaining.minus(step.multipliedBy(remaining.dividedBy(step))).isZero())
            throw new IllegalArgumentException("Time range must be divided into steps.");

        var schedules = new ArrayList<LocalTimeSchedule>();
        var from = startTime;
        while (remaining.isPositive()) {
            var to = from.plus(step);
            schedules.add(LocalTimeSchedule.of(from, to));
            from = to;
            remaining = remaining.minus(step);
        }

        return List.copyOf(schedules);
    }
}