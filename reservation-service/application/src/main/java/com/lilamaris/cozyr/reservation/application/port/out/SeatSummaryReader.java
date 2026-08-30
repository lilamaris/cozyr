package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.model.seat.SeatSummary;

import java.util.List;

public interface SeatSummaryReader {
    List<SeatSummary> find(long roomId);
}
