package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.domain.Reservation;

import java.util.Optional;
import java.util.UUID;

public interface ReservationStore {
    Reservation save(Reservation reservation);

    Optional<Reservation> findById(UUID reservationId);
}
