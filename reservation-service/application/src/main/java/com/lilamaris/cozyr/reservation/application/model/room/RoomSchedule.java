package com.lilamaris.cozyr.reservation.application.model.room;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.contract.model.LocalTimeSchedule;

import java.time.LocalTime;
import java.util.UUID;

public record RoomSchedule(
        UUID slotId,
        LocalTime startAt,
        LocalTime endAt
) {
    public RoomSchedule {
        ObjectPrecondition.requireNonNull(slotId, "slotId");
        ObjectPrecondition.requireNonNull(startAt, "startAt");
        ObjectPrecondition.requireNonNull(endAt, "endAt");
    }

    public static RoomSchedule of(UUID slotId, LocalTime startAt, LocalTime endAt) {
        return new RoomSchedule(slotId, startAt, endAt);
    }

    public static RoomSchedule from(UUID slotId, LocalTimeSchedule schedule) {
        return new RoomSchedule(slotId, schedule.from(), schedule.to());
    }

    public LocalTimeSchedule toLocalTimeSchedule() {
        return LocalTimeSchedule.of(startAt, endAt);
    }
}
