package com.lilamaris.cozyr.reservation.application.port.in;

import com.lilamaris.cozyr.reservation.application.model.seat.SeatSummary;
import com.lilamaris.cozyr.reservation.application.port.in.query.ListSeatSummaryQuery;

import java.util.List;

public interface ListSeatSummaryUseCase {
    List<SeatSummary> list(ListSeatSummaryQuery query);
}
