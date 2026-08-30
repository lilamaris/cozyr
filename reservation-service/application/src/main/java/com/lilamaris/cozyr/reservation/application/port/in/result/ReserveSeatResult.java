package com.lilamaris.cozyr.reservation.application.port.in.result;

import com.lilamaris.cozyr.reservation.domain.Reservation;

import java.time.Instant;
import java.util.UUID;

public record ReserveSeatResult(
        UUID reservationId,
        UUID reserveUserId,
        Instant createdAt,
        Instant updatedAt
) {
    public static ReserveSeatResult from(Reservation reservation) {
        return new ReserveSeatResult(
                reservation.getId(),
                reservation.getReservedUserId(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt()
        );
    }
}
