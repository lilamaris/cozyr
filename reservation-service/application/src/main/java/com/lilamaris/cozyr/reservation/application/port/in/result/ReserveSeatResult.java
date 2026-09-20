package com.lilamaris.cozyr.reservation.application.port.in.result;

import com.lilamaris.cozyr.reservation.application.model.room.RoomSchedule;
import com.lilamaris.cozyr.reservation.application.model.seat.SeatLocator;
import com.lilamaris.cozyr.reservation.contract.event.ReservationCreatedEvent;
import com.lilamaris.cozyr.reservation.domain.Reservation;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record ReserveSeatResult(
        UUID reservationId,
        LocalDate reservationDate,
        SeatLocator seatLocator,
        UUID reservationUserId,
        Instant createdAt,
        Set<RoomSchedule> schedules
) {
    public static ReserveSeatResult of(UUID reservationId, LocalDate reservationDate, SeatLocator seatLocator, UUID reservationUserId, Instant createdAt, Set<RoomSchedule> schedules) {
        return new ReserveSeatResult(reservationId, reservationDate, seatLocator, reservationUserId, createdAt, schedules);
    }

    public ReservationCreatedEvent toEvent() {
        return ReservationCreatedEvent.of(
                reservationId,
                reservationDate,
                seatLocator.roomId().getValue(),
                seatLocator.seatId().getValue(),
                reservationUserId,
                schedules.stream().map(RoomSchedule::toLocalTimeSchedule).toList()
        );
    }
}
