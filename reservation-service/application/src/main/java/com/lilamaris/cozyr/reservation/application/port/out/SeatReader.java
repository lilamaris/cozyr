package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.domain.Seat;
import com.lilamaris.cozyr.reservation.domain.SeatId;

import java.util.Optional;

public interface SeatReader {
    boolean existsById(SeatId id);

    Optional<Seat> findById(SeatId id);
}
