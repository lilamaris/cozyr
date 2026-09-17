package com.lilamaris.cozyr.reservation.jdbc.row;

import com.lilamaris.cozyr.reservation.application.model.room.RoomContext;
import com.lilamaris.cozyr.reservation.domain.RoomId;
import com.lilamaris.cozyr.reservation.domain.RoomOpPolicy;

import java.time.Instant;
import java.util.UUID;

public record RoomContextRow(
        UUID roomId,
        UUID roomOpPolicyId,
        int maxReservationPerUserPerDay,
        int maxSchedulePerReservation,
        Instant updatedAt
) {
    public RoomContext toModel() {
        var opPolicy = RoomOpPolicy.of(RoomId.of(roomId), maxReservationPerUserPerDay, maxSchedulePerReservation, updatedAt);
        return RoomContext.of(roomId, opPolicy);
    }
}
