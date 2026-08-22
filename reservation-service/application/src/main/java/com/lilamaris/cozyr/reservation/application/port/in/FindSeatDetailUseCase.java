package com.lilamaris.cozyr.reservation.application.port.in;

import com.lilamaris.cozyr.reservation.application.model.seat.SeatDetail;
import com.lilamaris.cozyr.reservation.application.port.in.query.FindSeatDetailQuery;

public interface FindSeatDetailUseCase {
    SeatDetail find(FindSeatDetailQuery query);
}
