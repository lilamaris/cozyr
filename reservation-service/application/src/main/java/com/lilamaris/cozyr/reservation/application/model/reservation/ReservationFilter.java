package com.lilamaris.cozyr.reservation.application.model.reservation;

import com.lilamaris.cozyr.reservation.domain.ReservationStatus;
import jakarta.annotation.Nullable;

import java.util.Set;
import java.util.UUID;

public record ReservationFilter(
        @Nullable UUID reservedUserId,
        @Nullable Set<ReservationStatus> statuses,
        @Nullable Long roomId,
        @Nullable String seatId
) {
    public static ReservationFilter empty() {
        return new ReservationFilter(null, null, null, null);
    }

    public ReservationFilter withReservedUserId(UUID reservedUserId) {
        return new ReservationFilter(reservedUserId, statuses, roomId, seatId);
    }

    public ReservationFilter withStatuses(Set<ReservationStatus> statuses) {
        return new ReservationFilter(reservedUserId, statuses, roomId, seatId);
    }

    public ReservationFilter withRoomId(Long roomId) {
        return new ReservationFilter(reservedUserId, statuses, roomId, seatId);
    }

    public ReservationFilter withSeatId(String seatId) {
        return new ReservationFilter(reservedUserId, statuses, roomId, seatId);
    }
}
