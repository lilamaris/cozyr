package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.domain.Seat;

public interface SeatStore {
    Seat save(Seat room);
}
