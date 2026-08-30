package com.lilamaris.cozyr.reservation.application.port.in.query;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;

public record ListSeatSummaryQuery(
        long roomId
) {
    public ListSeatSummaryQuery {
        NumberPrecondition.requireNonNegative(roomId, "roomId");
    }

    public static ListSeatSummaryQuery of(long roomId) {
        return new ListSeatSummaryQuery(roomId);
    }
}
