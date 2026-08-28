package com.lilamaris.cozyr.reservation.application.port.in.query;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;

public record FindRoomDetailQuery(
        long roomId
) {
    public FindRoomDetailQuery {
        NumberPrecondition.requireNonNegative(roomId, "roomId");
    }

    public static FindRoomDetailQuery of(long roomId) {
        return new FindRoomDetailQuery(roomId);
    }
}
