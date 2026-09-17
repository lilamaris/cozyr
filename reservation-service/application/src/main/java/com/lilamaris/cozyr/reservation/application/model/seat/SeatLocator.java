package com.lilamaris.cozyr.reservation.application.model.seat;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.domain.RoomId;
import com.lilamaris.cozyr.reservation.domain.SeatId;

import java.util.UUID;

public record SeatLocator(
        RoomId roomId,
        SeatId seatId
) {
    public SeatLocator {
        ObjectPrecondition.requireNonNull(roomId, "roomId");
        ObjectPrecondition.requireNonNull(seatId, "seatId");
    }

    public static SeatLocator of(RoomId roomId, SeatId seatId) {
        return new SeatLocator(roomId, seatId);
    }

    public static SeatLocator of(UUID roomId, UUID seatId) {
        return new SeatLocator(
                RoomId.of(roomId),
                SeatId.of(seatId)
        );
    }
}
