package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.domain.Reservation;

import java.util.Optional;
import java.util.UUID;

public interface ReservationReader {
    boolean existsById(UUID reservationId);

    Optional<Reservation> findById(UUID reservationId);
}
