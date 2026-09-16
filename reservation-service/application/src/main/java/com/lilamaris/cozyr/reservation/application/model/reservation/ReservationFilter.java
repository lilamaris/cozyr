package com.lilamaris.cozyr.reservation.application.model.reservation;

import com.lilamaris.cozyr.reservation.domain.ReservationStatus;
import com.lilamaris.cozyr.reservation.domain.RoomId;
import com.lilamaris.cozyr.reservation.domain.SeatId;
import jakarta.annotation.Nullable;

import java.util.Set;
import java.util.UUID;

public record ReservationFilter(
        @Nullable UUID reservedUserId,
        @Nullable Set<ReservationStatus> statuses,
        @Nullable RoomId roomId,
        @Nullable SeatId seatId
) {
    public static ReservationFilter empty() {
        return new ReservationFilter(null, null, null, null);
    }

    public ReservationFilter withReservedUserId(@Nullable UUID reservedUserId) {
        return new ReservationFilter(reservedUserId, statuses, roomId, seatId);
    }

    public ReservationFilter withStatuses(@Nullable Set<ReservationStatus> statuses) {
        return new ReservationFilter(reservedUserId, statuses, roomId, seatId);
    }

    public ReservationFilter withRoomId(@Nullable UUID roomId) {
        return new ReservationFilter(reservedUserId, statuses, roomId == null ? null : RoomId.of(roomId), seatId);
    }

    public ReservationFilter withSeatId(@Nullable UUID seatId) {
        return new ReservationFilter(reservedUserId, statuses, roomId, seatId == null ? null : SeatId.of(seatId));
    }
}
