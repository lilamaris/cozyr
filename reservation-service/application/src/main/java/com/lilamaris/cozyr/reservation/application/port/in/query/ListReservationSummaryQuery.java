package com.lilamaris.cozyr.reservation.application.port.in.query;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationCursor;
import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationFilter;
import jakarta.annotation.Nullable;

public record ListReservationSummaryQuery(
        ReservationFilter filter,
        @Nullable ReservationCursor cursor,
        int size
) {
    public ListReservationSummaryQuery {
        ObjectPrecondition.requireNonNull(filter, "filter");
        NumberPrecondition.requirePositive(size, "size");
    }

    public static ListReservationSummaryQuery of(ReservationFilter filter, @Nullable ReservationCursor cursor, int size) {
        return new ListReservationSummaryQuery(filter, cursor, size);
    }
}
