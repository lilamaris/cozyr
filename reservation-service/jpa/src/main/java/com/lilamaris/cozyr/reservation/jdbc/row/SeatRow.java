package com.lilamaris.cozyr.reservation.jdbc.row;

import com.lilamaris.cozyr.reservation.application.model.seat.SeatDetail;
import com.lilamaris.cozyr.reservation.application.model.seat.SeatSummary;
import com.lilamaris.cozyr.reservation.domain.SeatId;

import java.time.Instant;

public class SeatRow {
    public record Summary(
            long roomId,
            String seatId,
            Instant createdAt,
            Instant updatedAt
    ) {
        public SeatSummary toSummary() {
            return SeatSummary.of(SeatId.of(roomId, seatId), createdAt, updatedAt);
        }
    }

    public record Detail(
            long roomId,
            String seatId,
            Instant createdAt,
            Instant updatedAt
    ) {
        public SeatDetail toModel() {
            var id = SeatId.of(roomId, seatId);
            return SeatDetail.of(id, createdAt, updatedAt);
        }
    }
}
