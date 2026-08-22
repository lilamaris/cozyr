package com.lilamaris.cozyr.reservation.application.port.in.query;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.domain.SeatId;

public record FindSeatDetailQuery(
        SeatId id
) {
    public FindSeatDetailQuery {
        ObjectPrecondition.requireNonNull(id, "id");
    }

    public static FindSeatDetailQuery of(SeatId id) {
        return new FindSeatDetailQuery(id);
    }
}
