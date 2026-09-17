package com.lilamaris.cozyr.reservation.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.CollectionPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.application.model.seat.SeatLocator;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record ReserveSeatCommand(
        UUID reserveUserId,
        SeatLocator seatLocator,
        LocalDate reserveDate,
        Set<UUID> scheduleSlotIds
) {
    public ReserveSeatCommand {
        ObjectPrecondition.requireNonNull(reserveUserId, "reserveUserId");
        ObjectPrecondition.requireNonNull(seatLocator, "seatLocator");
        ObjectPrecondition.requireNonNull(reserveDate, "reserveDate");
        CollectionPrecondition.requireNonNullElements(scheduleSlotIds, "scheduleSlotIds");
    }

    public static ReserveSeatCommand of(UUID reserveUserId, SeatLocator seatLocator, LocalDate reserveDate, Set<UUID> scheduleSlotIds) {
        return new ReserveSeatCommand(reserveUserId, seatLocator, reserveDate, scheduleSlotIds);
    }

    public static ReserveSeatCommand of(UUID reserveUserId, UUID roomId, UUID seatId, LocalDate reserveDate, Set<UUID> scheduleSlotIds) {
        return new ReserveSeatCommand(reserveUserId, SeatLocator.of(roomId, seatId), reserveDate, scheduleSlotIds);
    }
}
