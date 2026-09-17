package com.lilamaris.cozyr.reservation.application.port.in.query;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.domain.RoomId;

import java.util.UUID;

public record FindRoomDetailQuery(
        RoomId roomId
) {
    public FindRoomDetailQuery {
        ObjectPrecondition.requireNonNull(roomId, "roomId");
    }

    public static FindRoomDetailQuery of(UUID roomId) {
        return new FindRoomDetailQuery(RoomId.of(roomId));
    }
}
