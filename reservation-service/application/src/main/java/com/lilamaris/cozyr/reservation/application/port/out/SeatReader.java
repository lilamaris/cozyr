package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.model.seat.SeatLocator;

public interface SeatReader {
    boolean existsByLocator(SeatLocator seatLocator);
}
