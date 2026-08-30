package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.domain.Reservation;

import java.util.UUID;

public interface ReservationStore {
    boolean existsById(UUID reservationId);

    Reservation save(Reservation reservation);
}
