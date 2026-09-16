package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.model.seat.SeatSummary;
import com.lilamaris.cozyr.reservation.domain.RoomId;

import java.util.List;

public interface SeatSummaryReader {
    List<SeatSummary> find(RoomId roomId);
}
