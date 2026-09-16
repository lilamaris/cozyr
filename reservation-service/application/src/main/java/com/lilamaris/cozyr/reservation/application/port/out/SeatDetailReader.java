package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.model.seat.SeatDetail;
import com.lilamaris.cozyr.reservation.application.model.seat.SeatLocator;

import java.util.Optional;

public interface SeatDetailReader {
    Optional<SeatDetail> findByLocator(SeatLocator seatLocator);
}
