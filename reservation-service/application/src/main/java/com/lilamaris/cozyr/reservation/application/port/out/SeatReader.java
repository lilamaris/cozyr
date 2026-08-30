package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.domain.SeatId;

public interface SeatReader {
    boolean existsById(SeatId id);
}
