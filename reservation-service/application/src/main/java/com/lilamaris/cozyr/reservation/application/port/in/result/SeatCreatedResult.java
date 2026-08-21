package com.lilamaris.cozyr.reservation.application.port.in.result;

import com.lilamaris.cozyr.reservation.domain.Seat;

import java.time.Instant;

public record SeatCreatedResult(
        long roomId,
        String seatId,
        Instant createdAt
) {
    public static SeatCreatedResult from(Seat seat) {
        return new SeatCreatedResult(
                seat.getId().getRoomId(),
                seat.getId().getSeatId(),
                seat.getCreatedAt()
        );
    }
}
