package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationContext;

import java.util.Optional;
import java.util.UUID;

public interface ReservationContextReader {
    Optional<ReservationContext> findById(UUID reservationId);
}
