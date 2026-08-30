package com.lilamaris.cozyr.reservation.application.port.in;

import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationCursor;
import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationSummary;
import com.lilamaris.cozyr.reservation.application.port.in.query.ListReservationSummaryQuery;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;

public interface ListReservationSummaryUseCase {
    CursorResult<ReservationSummary, ReservationCursor> list(ListReservationSummaryQuery query);
}
