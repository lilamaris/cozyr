package com.lilamaris.cozyr.reservation.jdbc.row;

import com.lilamaris.cozyr.reservation.application.model.seat.SeatDetail;
import com.lilamaris.cozyr.reservation.application.model.seat.SeatLocator;
import com.lilamaris.cozyr.reservation.application.model.seat.SeatSummary;

import java.time.Instant;
import java.util.UUID;

public class SeatRow {
    public record Summary(
            UUID roomId,
            UUID seatId,
            Instant createdAt,
            Instant updatedAt
    ) {
        public SeatSummary toSummary() {
            return SeatSummary.of(SeatLocator.of(roomId, seatId), createdAt, updatedAt);
        }
    }

    public record Detail(
            UUID roomId,
            UUID seatId,
            Instant createdAt,
            Instant updatedAt
    ) {
        public SeatDetail toModel() {
            return SeatDetail.of(SeatLocator.of(roomId, seatId), createdAt, updatedAt);
        }
    }
}
