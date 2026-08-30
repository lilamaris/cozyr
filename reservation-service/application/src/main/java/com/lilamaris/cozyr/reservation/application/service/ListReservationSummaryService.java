package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationCursor;
import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationSummary;
import com.lilamaris.cozyr.reservation.application.port.in.ListReservationSummaryUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.query.ListReservationSummaryQuery;
import com.lilamaris.cozyr.reservation.application.port.out.ReservationSummaryReader;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListReservationSummaryService implements ListReservationSummaryUseCase {
    private final ReservationSummaryReader reader;

    @Override
    public CursorResult<ReservationSummary, ReservationCursor> list(ListReservationSummaryQuery query) {
        var filter = query.filter();
        var request = CursorRequest.of(query.cursor(), query.size());
        return reader.find(filter, request);
    }
}
