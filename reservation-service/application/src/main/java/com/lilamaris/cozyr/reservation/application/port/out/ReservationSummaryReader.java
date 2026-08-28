package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationCursor;
import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationFilter;
import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationSummary;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;

public interface ReservationSummaryReader {
    CursorResult<ReservationSummary, ReservationCursor> find(ReservationFilter filter, CursorRequest<ReservationCursor> request);
}
