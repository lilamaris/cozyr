package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.model.seat.SeatDetail;
import com.lilamaris.cozyr.reservation.domain.SeatId;

import java.util.Optional;

public interface SeatDetailReader {
    Optional<SeatDetail> findById(SeatId id);
}
