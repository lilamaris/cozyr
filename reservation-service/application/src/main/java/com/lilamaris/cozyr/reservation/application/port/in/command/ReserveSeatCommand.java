package com.lilamaris.cozyr.reservation.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.CollectionPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.domain.SeatId;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record ReserveSeatCommand(
        UUID reserveUserId,
        SeatId reserveSeatId,
        LocalDate reserveDate,
        Set<UUID> scheduleSlotIds
) {
    public ReserveSeatCommand {
        ObjectPrecondition.requireNonNull(reserveUserId, "reserveUserId");
        ObjectPrecondition.requireNonNull(reserveSeatId, "reserveSeatId");
        ObjectPrecondition.requireNonNull(reserveDate, "reserveDate");
        CollectionPrecondition.requireNonNullElements(scheduleSlotIds, "scheduleSlotIds");
    }

    public static ReserveSeatCommand of(UUID reserveUserId, SeatId reserveSeatId, LocalDate reserveDate, Set<UUID> scheduleSlotIds) {
        return new ReserveSeatCommand(reserveUserId, reserveSeatId, reserveDate, scheduleSlotIds);
    }
}
