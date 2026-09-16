package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationContext;
import com.lilamaris.cozyr.reservation.domain.ReservationId;

import java.util.Optional;
import java.util.UUID;

public interface ReservationContextReader {
    Optional<ReservationContext> findById(ReservationId reservationId);
}
