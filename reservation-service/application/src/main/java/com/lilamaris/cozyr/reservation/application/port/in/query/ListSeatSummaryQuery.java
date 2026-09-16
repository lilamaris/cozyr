package com.lilamaris.cozyr.reservation.application.port.in.query;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.domain.RoomId;

import java.util.UUID;

public record ListSeatSummaryQuery(
        RoomId roomId
) {
    public ListSeatSummaryQuery {
        ObjectPrecondition.requireNonNull(roomId, "roomId");
    }

    public static ListSeatSummaryQuery of(UUID roomId) {
        return new ListSeatSummaryQuery(RoomId.of(roomId));
    }
}
