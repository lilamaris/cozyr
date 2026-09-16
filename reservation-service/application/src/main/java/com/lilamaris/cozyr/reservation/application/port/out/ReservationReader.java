package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.domain.Reservation;
import com.lilamaris.cozyr.reservation.domain.ReservationId;

import java.util.Optional;
import java.util.UUID;

public interface ReservationReader {
    boolean existsById(ReservationId reservationId);

    Optional<Reservation> findById(ReservationId reservationId);
}
